import { useCallback, useEffect, useRef, useState } from 'react';
import {
    KeyboardAvoidingView,
    Platform,
    StyleSheet,
    Text,
    TextInput,
    View,
    Pressable,
    ActivityIndicator,
    FlatList,
    Image,
    Modal,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useLocalSearchParams, Stack, useRouter } from 'expo-router';
import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { Audio } from 'expo-av';
import * as ImagePicker from 'expo-image-picker';
import * as MediaLibrary from 'expo-media-library';
import * as FileSystem from 'expo-file-system/legacy';
import { colors } from '../../constants/theme';
import { getSession } from '../../services/sessionService';
import {
    buildDirectChatId,
    markChatAsRead,
    sendMessageToChat,
    subscribeToMessages,
} from '../../services/chatFirebase';
import { hasRequiredFirebaseConfig } from '../../services/firebase';
import Toast from '../../components/Toast';
import SwipeBackGesture from '../../components/SwipeBackGesture';

const formatTime = (timestamp) => {
    if (!timestamp) {
        return '';
    }

    const date =
        typeof timestamp?.toDate === 'function'
            ? timestamp.toDate()
            : timestamp instanceof Date
                ? timestamp
                : null;

    if (!date) {
        return '';
    }

    return date.toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit',
    });
};

const getImageExtensionByUri = (uri) => {
    const normalizedUri = String(uri || '').toLowerCase();
    if (normalizedUri.includes('.png')) return 'png';
    if (normalizedUri.includes('.webp')) return 'webp';
    if (normalizedUri.includes('.heic') || normalizedUri.includes('.heif')) return 'heic';
    return 'jpg';
};

const formatAudioDuration = (durationMs) => {
    const totalSeconds = Math.max(0, Math.floor(Number(durationMs || 0) / 1000));
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
};

const getAudioMimeTypeByUri = (uri) => {
    const normalizedUri = String(uri || '').toLowerCase();

    if (normalizedUri.endsWith('.m4a') || normalizedUri.endsWith('.mp4')) return 'audio/mp4';
    if (normalizedUri.endsWith('.aac')) return 'audio/aac';
    if (normalizedUri.endsWith('.3gp') || normalizedUri.endsWith('.3gpp')) return 'audio/3gpp';
    if (normalizedUri.endsWith('.amr')) return 'audio/amr';
    if (normalizedUri.endsWith('.caf')) return 'audio/x-caf';
    if (normalizedUri.endsWith('.wav')) return 'audio/wav';
    if (normalizedUri.endsWith('.mp3')) return 'audio/mpeg';

    return 'audio/mp4';
};

const getStringParam = (value) => {
    if (typeof value === 'string') return value;
    if (Array.isArray(value) && typeof value[0] === 'string') return value[0];
    return '';
};

export default function Chat() {
    const insets = useSafeAreaInsets();
    const router = useRouter();
    const { userId: targetUserId, userName: targetUserName, backTo } = useLocalSearchParams();
    const [messages, setMessages] = useState([]);
    const [messageText, setMessageText] = useState('');
    const [isSending, setIsSending] = useState(false);
    const [currentOngId, setCurrentOngId] = useState('');
    const [currentOngName, setCurrentOngName] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [pendingImageUri, setPendingImageUri] = useState('');
    const [pendingImageDataUrl, setPendingImageDataUrl] = useState('');
    const [pendingImageDescription, setPendingImageDescription] = useState('');
    const [isPendingImageVisible, setIsPendingImageVisible] = useState(false);
    const [isImageSourceModalVisible, setIsImageSourceModalVisible] = useState(false);
    const [isSendingImage, setIsSendingImage] = useState(false);
    const [isSendingAudio, setIsSendingAudio] = useState(false);
    const [isRecordingAudio, setIsRecordingAudio] = useState(false);
    const [recordingAudioDurationMs, setRecordingAudioDurationMs] = useState(0);
    const [playingAudioMessageId, setPlayingAudioMessageId] = useState('');
    const [focusedImageUri, setFocusedImageUri] = useState('');
    const [isDownloadingImage, setIsDownloadingImage] = useState(false);
    const [toast, setToast] = useState({ visible: false, title: '', message: '', type: 'info' });
    const listRef = useRef(null);
    const recordingRef = useRef(null);
    const recordingTimerRef = useRef(null);
    const soundRef = useRef(null);
    const toastTimeoutRef = useRef(null);
    const isRecordPressActiveRef = useRef(false);
    const isRecordStartInProgressRef = useRef(false);

    const chatId = buildDirectChatId(currentOngId, targetUserId);

    const displayedMessages = messages.map((msg) => ({
        ...msg,
        isMine: String(msg.senderId || '') === String(currentOngId || ''),
    }));

    const showToast = (title, message, type = 'info', duration = 2400) => {
        if (toastTimeoutRef.current) {
            clearTimeout(toastTimeoutRef.current);
        }

        setToast({ visible: true, title, message, type });

        toastTimeoutRef.current = setTimeout(() => {
            setToast((prev) => ({ ...prev, visible: false }));
            toastTimeoutRef.current = null;
        }, duration);
    };

    const handleGestureBack = useCallback(() => {
        const backToPath = getStringParam(backTo).trim();

        if (backToPath.length > 0) {
            router.replace(backToPath);
            return;
        }

        if (typeof router.canGoBack === 'function' && router.canGoBack()) {
            router.back();
            return;
        }

        router.replace('/ong/interessados');
    }, [backTo, router]);

    useEffect(() => {
        const loadSession = async () => {
            try {
                const session = await getSession();
                const ongId = session?.ongId || '';
                const ongName = session?.nome || 'ONG';
                setCurrentOngId(ongId);
                setCurrentOngName(ongName);
            } catch (err) {
                setError('Erro ao carregar sessão');
            } finally {
                setIsLoading(false);
            }
        };

        loadSession();
    }, []);

    useEffect(() => {
        if (!hasRequiredFirebaseConfig || !chatId) {
            return () => { };
        }

        markChatAsRead(chatId, currentOngId).catch(() => { });

        const unsubscribe = subscribeToMessages(
            chatId,
            (nextMessages) => {
                setMessages(nextMessages);
                setTimeout(() => {
                    listRef.current?.scrollToEnd({ animated: false });
                }, 50);
                markChatAsRead(chatId, currentOngId).catch(() => { });
            },
            (err) => {
                setError(err?.message || 'Erro ao carregar mensagens');
            }
        );

        return () => {
            unsubscribe();
        };
    }, [chatId, currentOngId]);

    useEffect(() => () => {
        if (recordingTimerRef.current) {
            clearInterval(recordingTimerRef.current);
            recordingTimerRef.current = null;
        }

        if (recordingRef.current) {
            recordingRef.current.stopAndUnloadAsync().catch(() => { });
            recordingRef.current = null;
        }

        if (soundRef.current) {
            soundRef.current.unloadAsync().catch(() => { });
            soundRef.current = null;
        }

        if (toastTimeoutRef.current) {
            clearTimeout(toastTimeoutRef.current);
        }
    }, []);

    const handleSendText = async () => {
        const trim = messageText.trim();
        if (!trim || isSending || !hasRequiredFirebaseConfig) {
            return;
        }

        setIsSending(true);
        try {
            await sendMessageToChat({
                chatId,
                senderId: currentOngId,
                senderName: currentOngName,
                recipientId: targetUserId,
                recipientName: targetUserName,
                messageText: trim,
            });
            setMessageText('');
        } catch (err) {
            setError(err?.message || 'Erro ao enviar mensagem');
        } finally {
            setIsSending(false);
        }
    };

    const resetImageComposeState = () => {
        setPendingImageUri('');
        setPendingImageDataUrl('');
        setPendingImageDescription('');
        setIsPendingImageVisible(false);
    };

    const handlePickImage = async (source) => {
        try {
            const permission =
                source === 'camera'
                    ? await ImagePicker.requestCameraPermissionsAsync()
                    : await ImagePicker.requestMediaLibraryPermissionsAsync();

            if (!permission.granted) {
                setError(
                    source === 'camera'
                        ? 'Permissão da câmera não concedida.'
                        : 'Permissão da galeria não concedida.'
                );
                return;
            }

            const pickerOptions = {
                mediaTypes: ['images'],
                allowsEditing: true,
                quality: 0.75,
                base64: true,
            };

            const result =
                source === 'camera'
                    ? await ImagePicker.launchCameraAsync(pickerOptions)
                    : await ImagePicker.launchImageLibraryAsync(pickerOptions);

            if (!result.canceled && result.assets?.[0]) {
                const asset = result.assets[0];
                const imageMimeType = asset?.mimeType || 'image/jpeg';
                const imageBase64 = asset?.base64 || '';

                if (!imageBase64) {
                    setError('Não foi possível converter a imagem.');
                    return;
                }

                const imageDataUrl = `data:${imageMimeType};base64,${imageBase64}`;
                setPendingImageUri(asset?.uri || imageDataUrl);
                setPendingImageDataUrl(imageDataUrl);
                setPendingImageDescription('');
                setIsPendingImageVisible(true);
            }
        } catch (err) {
            setError(err?.message || 'Erro ao selecionar imagem');
        }
    };

    const handleOpenImageOptions = () => {
        if (isSendingImage) {
            return;
        }

        setIsImageSourceModalVisible(true);
    };

    const handlePickImageFromSource = async (source) => {
        setIsImageSourceModalVisible(false);
        await handlePickImage(source);
    };

    const handleSendImage = async () => {
        if (!pendingImageDataUrl || isSendingImage || !hasRequiredFirebaseConfig) {
            return;
        }

        setIsSendingImage(true);
        try {
            const caption = String(pendingImageDescription || '').trim();

            await sendMessageToChat({
                chatId,
                senderId: currentOngId,
                senderName: currentOngName,
                recipientId: targetUserId,
                recipientName: targetUserName,
                messageText: caption,
                imageDataUrl: pendingImageDataUrl,
            });

            resetImageComposeState();
        } catch (err) {
            setError(err?.message || 'Erro ao enviar imagem');
        } finally {
            setIsSendingImage(false);
        }
    };

    const startRecordingTimer = () => {
        if (recordingTimerRef.current) {
            clearInterval(recordingTimerRef.current);
            recordingTimerRef.current = null;
        }

        recordingTimerRef.current = setInterval(async () => {
            if (!recordingRef.current) return;

            try {
                const status = await recordingRef.current.getStatusAsync();
                if (status?.canRecord || status?.isRecording) {
                    setRecordingAudioDurationMs(Number(status.durationMillis || 0));
                }
            } catch {
                // Ignora falhas transitórias de status.
            }
        }, 250);
    };

    const stopRecordingTimer = () => {
        if (recordingTimerRef.current) {
            clearInterval(recordingTimerRef.current);
            recordingTimerRef.current = null;
        }
    };

    const startHoldToRecordAudio = async () => {
        if (isSendingAudio || isSendingImage || isRecordingAudio || isRecordStartInProgressRef.current) {
            return;
        }

        try {
            isRecordStartInProgressRef.current = true;
            setError('');

            const permission = await Audio.requestPermissionsAsync();
            if (!permission.granted) {
                setError('Permissão de microfone negada.');
                return;
            }

            await Audio.setAudioModeAsync({
                allowsRecordingIOS: true,
                playsInSilentModeIOS: true,
                staysActiveInBackground: false,
            });

            const recording = new Audio.Recording();
            await recording.prepareToRecordAsync(Audio.RecordingOptionsPresets.HIGH_QUALITY);
            await recording.startAsync();

            recordingRef.current = recording;
            setRecordingAudioDurationMs(0);
            setIsRecordingAudio(true);

            startRecordingTimer();

            if (!isRecordPressActiveRef.current) {
                stopHoldToRecordAndSendAudio();
            }
        } catch (err) {
            setIsRecordingAudio(false);
            setError(err?.message || 'Não foi possível iniciar a gravação.');
        } finally {
            isRecordStartInProgressRef.current = false;
        }
    };

    const stopHoldToRecordAndSendAudio = async () => {
        if (!recordingRef.current || isSendingAudio) {
            return;
        }

        setIsSendingAudio(true);
        try {
            const recording = recordingRef.current;
            recordingRef.current = null;

            stopRecordingTimer();

            const statusBeforeStop = await recording.getStatusAsync();
            const durationMs = Number(statusBeforeStop?.durationMillis || recordingAudioDurationMs || 0);

            await recording.stopAndUnloadAsync();
            const audioUri = recording.getURI();

            setIsRecordingAudio(false);

            await Audio.setAudioModeAsync({
                allowsRecordingIOS: false,
                playsInSilentModeIOS: true,
            });

            if (durationMs < 350) {
                setRecordingAudioDurationMs(0);
                return;
            }

            if (!audioUri) {
                throw new Error('Não foi possível obter o áudio gravado.');
            }

            await sendMessageToChat({
                chatId,
                senderId: currentOngId,
                senderName: currentOngName,
                recipientId: targetUserId,
                recipientName: targetUserName,
                audioFileUri: audioUri,
                audioMimeType: getAudioMimeTypeByUri(audioUri),
                audioDurationMs: durationMs,
            });
        } catch (err) {
            setError(err?.message || 'Erro ao enviar áudio');
        } finally {
            setIsRecordingAudio(false);
            setRecordingAudioDurationMs(0);
            setIsSendingAudio(false);

            Audio.setAudioModeAsync({
                allowsRecordingIOS: false,
                playsInSilentModeIOS: true,
            }).catch(() => { });
        }
    };

    const handleRecordPressIn = () => {
        isRecordPressActiveRef.current = true;
        startHoldToRecordAudio();
    };

    const handleRecordPressOut = () => {
        isRecordPressActiveRef.current = false;
        stopHoldToRecordAndSendAudio();
    };

    const handlePlayAudio = async (message) => {
        const audioUrl = String(message?.audioUrl || '').trim();

        if (!audioUrl) {
            return;
        }

        try {
            if (soundRef.current) {
                await soundRef.current.unloadAsync();
                soundRef.current = null;
            }

            if (playingAudioMessageId === message.id) {
                setPlayingAudioMessageId('');
                return;
            }

            const { sound } = await Audio.Sound.createAsync(
                { uri: audioUrl },
                { shouldPlay: true },
                (status) => {
                    if (status.didJustFinish) {
                        setPlayingAudioMessageId('');
                        soundRef.current?.unloadAsync().catch(() => { });
                        soundRef.current = null;
                    }
                }
            );

            soundRef.current = sound;
            setPlayingAudioMessageId(message.id);
        } catch (err) {
            setPlayingAudioMessageId('');
            setError(err?.message || 'Não foi possível reproduzir o áudio.');
        }
    };

    const handleDownloadFocusedImage = async () => {
        const imageUri = String(focusedImageUri || '').trim();

        if (!imageUri || isDownloadingImage) {
            return;
        }

        try {
            setIsDownloadingImage(true);

            const permission = await MediaLibrary.requestPermissionsAsync();

            if (!permission?.granted) {
                showToast('Permissão necessária', 'Autorize acesso à galeria para baixar a imagem.', 'warning');
                return;
            }

            const albumName = 'PeTinder';
            let localImageUri = imageUri;

            if (imageUri.startsWith('data:image/')) {
                const [metaPart, base64Part] = imageUri.split(',');

                if (!base64Part) {
                    throw new Error('Imagem inválida para download.');
                }

                const extensionMatch = metaPart.match(/^data:image\/(.+);base64$/i);
                const extension = extensionMatch?.[1]?.toLowerCase() || 'jpg';
                const cacheDir = FileSystem.cacheDirectory || FileSystem.documentDirectory;

                if (!cacheDir) {
                    throw new Error('Armazenamento local indisponível.');
                }

                const fileUri = `${cacheDir}petinder-image-${Date.now()}.${extension}`;

                await FileSystem.writeAsStringAsync(fileUri, base64Part, {
                    encoding: FileSystem.EncodingType.Base64,
                });

                localImageUri = fileUri;
            } else if (imageUri.startsWith('http://') || imageUri.startsWith('https://')) {
                const cacheDir = FileSystem.cacheDirectory || FileSystem.documentDirectory;

                if (!cacheDir) {
                    throw new Error('Armazenamento local indisponível.');
                }

                const extension = getImageExtensionByUri(imageUri);
                const fileUri = `${cacheDir}petinder-image-${Date.now()}.${extension}`;
                const downloadedFile = await FileSystem.downloadAsync(imageUri, fileUri);
                localImageUri = downloadedFile.uri;
            }

            const createdAsset = await MediaLibrary.createAssetAsync(localImageUri);
            const existingAlbum = await MediaLibrary.getAlbumAsync(albumName);

            if (!existingAlbum) {
                await MediaLibrary.createAlbumAsync(albumName, createdAsset, false);
            } else {
                await MediaLibrary.addAssetsToAlbumAsync([createdAsset], existingAlbum, false);
            }

            showToast('Download concluído', `Imagem salva no álbum ${albumName}.`, 'success');
        } catch (err) {
            setError(err?.message || 'Não foi possível baixar a imagem.');
        } finally {
            setIsDownloadingImage(false);
        }
    };

    if (isLoading) {
        return (
            <View style={styles.container}>
                <ActivityIndicator size="large" color={colors.mauve} />
            </View>
        );
    }

    if (!hasRequiredFirebaseConfig) {
        return (
            <View style={styles.container}>
                <Text style={styles.errorText}>
                    Firebase não configurado. Defina EXPO_PUBLIC_FIREBASE_* no .env.
                </Text>
            </View>
        );
    }

    return (
        <>
            <Stack.Screen
                options={{
                    title: targetUserName || 'Chat',
                    headerShown: true,
                    headerBackVisible: true,
                }}
            />
            <SwipeBackGesture onSwipeBack={handleGestureBack}>
                <KeyboardAvoidingView
                    style={styles.container}
                    behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
                    keyboardVerticalOffset={Platform.OS === 'ios' ? insets.top + 50 : insets.top + 60}
                    enabled={true}
                >
                    {Boolean(error) && <Text style={styles.errorBanner}>{error}</Text>}

                    <FlatList
                        ref={listRef}
                        data={displayedMessages}
                        style={styles.messagesList}
                        contentContainerStyle={styles.messagesListContent}
                        keyExtractor={(item) => item.id}
                        onContentSizeChange={() => listRef.current?.scrollToEnd({ animated: true })}
                        renderItem={({ item }) => (
                            <View
                                style={[
                                    styles.messageBubble,
                                    item.isMine ? styles.myMessage : styles.theirMessage,
                                ]}
                            >
                                {!item.isMine && (
                                    <Text style={styles.senderName}>{item.senderName || 'Usuário'}</Text>
                                )}
                                {Boolean(item?.imageUrl) && (
                                    <Pressable onPress={() => setFocusedImageUri(String(item.imageUrl || ''))}>
                                        <Image
                                            source={{ uri: item.imageUrl }}
                                            style={styles.messageImage}
                                            resizeMode="cover"
                                        />
                                    </Pressable>
                                )}
                                {Boolean(item?.text) && (
                                    <Text style={styles.messageText}>{item.text}</Text>
                                )}
                                {Boolean(item?.audioUrl) && (
                                    <Pressable
                                        onPress={() => handlePlayAudio(item)}
                                        style={styles.audioMessageButton}
                                    >
                                        <MaterialIcons
                                            name={playingAudioMessageId === item.id ? 'stop-circle' : 'play-circle-filled'}
                                            size={24}
                                            color="#FFFFFF"
                                        />
                                        <Text style={styles.audioMessageText}>
                                            {playingAudioMessageId === item.id ? 'Parar áudio' : 'Ouvir áudio'}
                                        </Text>
                                        <Text style={styles.audioMessageDurationText}>
                                            {formatAudioDuration(item?.audioDurationMs)}
                                        </Text>
                                    </Pressable>
                                )}
                                <Text style={styles.messageTime}>{formatTime(item.createdAt)}</Text>
                            </View>
                        )}
                        ListEmptyComponent={
                            <View style={styles.emptyContainer}>
                                <Text style={styles.emptyText}>Nenhuma mensagem ainda</Text>
                            </View>
                        }
                    />

                    <View style={[styles.inputContainer, { paddingBottom: insets.bottom || 8 }]}>
                        <Pressable
                            style={({ pressed }) => [
                                styles.attachButton,
                                (isSendingImage || isSendingAudio) && styles.buttonDisabled,
                                pressed && styles.buttonPressed,
                            ]}
                            onPress={handleOpenImageOptions}
                            disabled={isSendingImage || isSendingAudio}
                        >
                            <MaterialIcons name="image" size={20} color="#fff" />
                        </Pressable>

                        <Pressable
                            style={({ pressed }) => [
                                styles.attachButton,
                                isRecordingAudio && styles.recordButtonActive,
                                (isSendingAudio || isSending || isSendingImage) && styles.buttonDisabled,
                                pressed && styles.buttonPressed,
                            ]}
                            onPressIn={handleRecordPressIn}
                            onPressOut={handleRecordPressOut}
                            disabled={isSendingAudio || isSending || isSendingImage}
                        >
                            <MaterialIcons name={isRecordingAudio ? 'graphic-eq' : 'mic'} size={20} color="#fff" />
                        </Pressable>

                        <TextInput
                            style={styles.input}
                            placeholder="Digite uma mensagem..."
                            placeholderTextColor="#999"
                            value={messageText}
                            onChangeText={setMessageText}
                            editable={!isSending && !isSendingImage && !isSendingAudio}
                            multiline
                            maxLength={500}
                        />
                        <Pressable
                            style={({ pressed }) => [
                                styles.sendButton,
                                (isSending || isSendingImage || isSendingAudio) && styles.buttonDisabled,
                                pressed && styles.buttonPressed,
                            ]}
                            onPress={handleSendText}
                            disabled={isSending || isSendingImage || isSendingAudio}
                        >
                            {isSending ? (
                                <ActivityIndicator size="small" color="#fff" />
                            ) : (
                                <MaterialIcons name="send" size={18} color="#fff" />
                            )}
                        </Pressable>
                    </View>

                    {isRecordingAudio && (
                        <View style={styles.recordingHintRow}>
                            <View style={styles.recordingDot} />
                            <Text style={styles.recordingHintText}>
                                Gravando... solte para enviar {formatAudioDuration(recordingAudioDurationMs)}
                            </Text>
                        </View>
                    )}

                    <Modal
                        visible={isPendingImageVisible}
                        transparent
                        animationType="fade"
                        onRequestClose={() => !isSendingImage && resetImageComposeState()}
                    >
                        <View style={styles.modalOverlay}>
                            <View style={styles.composeModalCard}>
                                {(pendingImageUri || pendingImageDataUrl) && (
                                    <Image
                                        source={{ uri: pendingImageUri || pendingImageDataUrl }}
                                        style={styles.previewImage}
                                        resizeMode="cover"
                                    />
                                )}

                                <TextInput
                                    value={pendingImageDescription}
                                    onChangeText={setPendingImageDescription}
                                    placeholder="Adicione uma descrição..."
                                    placeholderTextColor="#9A9A9A"
                                    style={styles.composeInput}
                                    editable={!isSendingImage}
                                    maxLength={280}
                                />

                                <View style={styles.modalActions}>
                                    <Pressable
                                        style={({ pressed }) => [
                                            styles.modalButton,
                                            styles.cancelButton,
                                            pressed && styles.buttonPressed,
                                        ]}
                                        onPress={() => !isSendingImage && resetImageComposeState()}
                                        disabled={isSendingImage}
                                    >
                                        <Text style={styles.modalButtonText}>Cancelar</Text>
                                    </Pressable>

                                    <Pressable
                                        style={({ pressed }) => [
                                            styles.modalButton,
                                            styles.sendImageButton,
                                            (isSendingImage || !pendingImageDataUrl) && styles.buttonDisabled,
                                            pressed && styles.buttonPressed,
                                        ]}
                                        onPress={handleSendImage}
                                        disabled={isSendingImage || !pendingImageDataUrl}
                                    >
                                        {isSendingImage ? (
                                            <ActivityIndicator size="small" color="#fff" />
                                        ) : (
                                            <Text style={styles.modalButtonText}>Enviar</Text>
                                        )}
                                    </Pressable>
                                </View>
                            </View>
                        </View>
                    </Modal>

                    <Modal
                        visible={isImageSourceModalVisible}
                        transparent
                        animationType="fade"
                        onRequestClose={() => setIsImageSourceModalVisible(false)}
                    >
                        <View style={styles.optionModalOverlay}>
                            <Pressable
                                style={styles.optionModalBackdrop}
                                onPress={() => setIsImageSourceModalVisible(false)}
                            />

                            <View style={styles.optionModalCard}>
                                <Text style={styles.optionModalTitle}>Enviar imagem</Text>
                                <Text style={styles.optionModalSubtitle}>Escolha uma opção</Text>

                                <Pressable
                                    style={({ pressed }) => [styles.optionButton, pressed && styles.buttonPressed]}
                                    onPress={() => handlePickImageFromSource('camera')}
                                >
                                    <Text style={styles.optionButtonText}>Câmera</Text>
                                </Pressable>

                                <Pressable
                                    style={({ pressed }) => [styles.optionButton, pressed && styles.buttonPressed]}
                                    onPress={() => handlePickImageFromSource('gallery')}
                                >
                                    <Text style={styles.optionButtonText}>Galeria</Text>
                                </Pressable>

                                <Pressable
                                    style={({ pressed }) => [styles.optionButton, styles.optionCancelButton, pressed && styles.buttonPressed]}
                                    onPress={() => setIsImageSourceModalVisible(false)}
                                >
                                    <Text style={styles.optionButtonText}>Cancelar</Text>
                                </Pressable>
                            </View>
                        </View>
                    </Modal>

                    <Modal
                        visible={Boolean(focusedImageUri)}
                        transparent
                        animationType="fade"
                        onRequestClose={() => setFocusedImageUri('')}
                    >
                        <View style={styles.focusModalRoot}>
                            <Pressable style={styles.focusModalBackdrop} onPress={() => setFocusedImageUri('')} />

                            <View style={styles.focusImageContainer}>
                                <Image
                                    source={{ uri: focusedImageUri }}
                                    style={styles.focusImage}
                                    resizeMode="contain"
                                />
                            </View>

                            <Pressable
                                style={[styles.focusActionButton, styles.focusDownloadButton]}
                                onPress={handleDownloadFocusedImage}
                                disabled={isDownloadingImage}
                            >
                                {isDownloadingImage ? (
                                    <ActivityIndicator size="small" color="#FFFFFF" />
                                ) : (
                                    <MaterialIcons name="download" size={22} color="#FFFFFF" />
                                )}
                            </Pressable>

                            <Pressable
                                style={[styles.focusActionButton, styles.focusCloseButton]}
                                onPress={() => setFocusedImageUri('')}
                            >
                                <MaterialIcons name="close" size={22} color="#FFFFFF" />
                            </Pressable>
                        </View>
                    </Modal>

                    <Toast visible={toast.visible} title={toast.title} message={toast.message} type={toast.type} />
                </KeyboardAvoidingView>
            </SwipeBackGesture>
        </>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.white,
    },
    errorBanner: {
        backgroundColor: '#ffebee',
        color: '#c62828',
        padding: 12,
        fontSize: 12,
        fontWeight: '500',
    },
    errorText: {
        color: '#c62828',
        fontSize: 14,
    },
    messagesList: {
        flex: 1,
    },
    messagesListContent: {
        paddingHorizontal: 16,
        paddingVertical: 8,
        gap: 8,
    },
    messageBubble: {
        borderRadius: 12,
        padding: 12,
        maxWidth: '80%',
        marginVertical: 4,
    },
    myMessage: {
        alignSelf: 'flex-end',
        backgroundColor: "#fa8bae",
    },
    theirMessage: {
        alignSelf: 'flex-start',
        backgroundColor: colors.roseSurface,
    },
    senderName: {
        fontSize: 11,
        fontWeight: '600',
        color: '#999',
        marginBottom: 4,
    },
    messageImage: {
        width: 200,
        height: 150,
        borderRadius: 8,
        marginBottom: 4,
    },
    messageText: {
        fontSize: 14,
        color: colors.textStrong,
        marginBottom: 4,
    },
    messageTime: {
        fontSize: 11,
        color: '#272727',
    },
    emptyContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        paddingHorizontal: 24,
    },
    emptyText: {
        fontSize: 14,
        color: '#999',
        textAlign: 'center',
    },
    inputContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingHorizontal: 12,
        paddingTop: 8,
        borderTopWidth: 1,
        borderTopColor: colors.roseSurface,
        gap: 8,
        backgroundColor: colors.white,
    },
    attachButton: {
        width: 40,
        height: 40,
        borderRadius: 10,
        backgroundColor: colors.mauve,
        justifyContent: 'center',
        alignItems: 'center',
    },
    input: {
        flex: 1,
        borderWidth: 1,
        borderColor: colors.roseBorder,
        borderRadius: 12,
        paddingHorizontal: 12,
        paddingVertical: 6,
        minHeight: 40,
        maxHeight: 80,
        alignSelf: 'center',
        textAlignVertical: 'center',
        fontSize: 14,
        maxHeight: 100,
        color: colors.textStrong,
    },
    sendButton: {
        width: 40,
        height: 40,
        borderRadius: 10,
        backgroundColor: colors.mauve,
        justifyContent: 'center',
        alignItems: 'center',
    },
    buttonDisabled: {
        opacity: 0.6,
    },
    buttonPressed: {
        opacity: 0.8,
    },
    recordButtonActive: {
        backgroundColor: '#C53764',
    },
    recordingHintRow: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 6,
        paddingHorizontal: 14,
        paddingBottom: 8,
    },
    recordingDot: {
        width: 8,
        height: 8,
        borderRadius: 4,
        backgroundColor: '#FF5E87',
    },
    recordingHintText: {
        color: '#80465D',
        fontSize: 12,
        fontWeight: '500',
    },
    audioMessageButton: {
        marginBottom: 6,
        borderRadius: 12,
        backgroundColor: 'rgb(243, 122, 197)',
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
        width: 200,
        paddingHorizontal: 10,
        paddingVertical: 8,
    },
    audioMessageText: {
        color: '#FFFFFF',
        fontSize: 13,
        flex: 1,
        fontWeight: '500',
    },
    audioMessageDurationText: {
        color: '#ffffff',
        fontSize: 11,
    },
    modalOverlay: {
        flex: 1,
        backgroundColor: 'rgba(0,0,0,0.9)',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 16,
    },
    composeModalCard: {
        width: '92%',
        borderRadius: 14,
        backgroundColor: '#1E1E1E',
        overflow: 'hidden',
        paddingBottom: 12,
    },
    previewImage: {
        width: '100%',
        height: 300,
        backgroundColor: '#111111',
    },
    composeInput: {
        marginTop: 10,
        marginHorizontal: 12,
        borderRadius: 10,
        backgroundColor: '#2C2C2C',
        color: '#FFFFFF',
        paddingHorizontal: 12,
        paddingVertical: 10,
        fontSize: 14,
    },
    modalActions: {
        marginTop: 10,
        flexDirection: 'row',
        gap: 12,
        justifyContent: 'flex-end',
        paddingHorizontal: 12,
    },
    modalButton: {
        paddingHorizontal: 20,
        paddingVertical: 12,
        borderRadius: 10,
        minWidth: 100,
        justifyContent: 'center',
        alignItems: 'center',
    },
    cancelButton: {
        backgroundColor: '#999',
    },
    sendImageButton: {
        backgroundColor: colors.mauve,
    },
    modalButtonText: {
        color: '#fff',
        fontWeight: '600',
        fontSize: 14,
    },
    optionModalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 16,
    },
    optionModalBackdrop: {
        ...StyleSheet.absoluteFillObject,
        backgroundColor: 'rgba(0,0,0,0.45)',
    },
    optionModalCard: {
        width: '90%',
        maxWidth: 360,
        backgroundColor: '#FFFFFF',
        borderRadius: 14,
        padding: 16,
        gap: 10,
    },
    optionModalTitle: {
        fontSize: 18,
        fontWeight: '700',
        color: colors.textStrong,
        textAlign: 'center',
    },
    optionModalSubtitle: {
        fontSize: 13,
        color: '#666666',
        textAlign: 'center',
        marginBottom: 4,
    },
    optionButton: {
        borderRadius: 10,
        paddingVertical: 11,
        alignItems: 'center',
        backgroundColor: colors.mauve,
    },
    optionCancelButton: {
        backgroundColor: '#8D8D8D',
    },
    optionButtonText: {
        color: '#FFFFFF',
        fontSize: 14,
        fontWeight: '600',
    },
    focusModalRoot: {
        flex: 1,
        backgroundColor: 'rgba(0,0,0,0.92)',
        justifyContent: 'center',
        alignItems: 'center',
    },
    focusModalBackdrop: {
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
    },
    focusImageContainer: {
        width: '92%',
        height: '78%',
        borderRadius: 14,
        overflow: 'hidden',
        backgroundColor: '#111111',
    },
    focusImage: {
        width: '100%',
        height: '100%',
    },
    focusActionButton: {
        position: 'absolute',
        top: 46,
        width: 40,
        height: 40,
        borderRadius: 20,
        backgroundColor: 'rgba(255,255,255,0.12)',
        justifyContent: 'center',
        alignItems: 'center',
    },
    focusDownloadButton: {
        left: 18,
    },
    focusCloseButton: {
        right: 18,
    },
});
