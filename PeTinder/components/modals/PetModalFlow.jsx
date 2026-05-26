import { useCallback, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { useRouter } from 'expo-router';
import { colors } from '../../constants/theme';
import Toast from '../Toast';
import {
    deletarPet,
    fetchAdotanteInfoPetCard,
    fetchAdotantesInteressados,
    marcarPetComoAdotado,
    marcarComoAdotadoExterno,
    voltarParaAdocao,
} from '../../services/petApiService';
import { getSession } from '../../services/sessionService';
import GenericModal from '../GenericModal';
import DynamicButton from '../DynamicButton';
import PetDetailsModal from './PetDetailsModal';
import AdoptionStatusModal from './AdoptionStatusModal';
import AdopterInfoModal from './AdopterInfoModal';
import InterestedUsersModal from './InterestedUsersModal';

const ADOTANTE_EXTERNO_ID = '11111111-1111-1111-1111-111111111111';

export default function PetModalFlow({ visible, pet, onClose, onRefresh }) {
    const router = useRouter();
    const [isStatusModalVisible, setIsStatusModalVisible] = useState(false);
    const [isAdotanteModalVisible, setIsAdotanteModalVisible] = useState(false);
    const [isInteressadosModalVisible, setIsInteressadosModalVisible] = useState(false);
    const [isDeleteConfirmModalVisible, setIsDeleteConfirmModalVisible] = useState(false);
    const [interessados, setInteressados] = useState([]);
    const [isLoadingInteressados, setIsLoadingInteressados] = useState(false);
    const [adotanteInfo, setAdotanteInfo] = useState(null);
    const [toast, setToast] = useState({ visible: false, title: '', message: '', type: 'info' });

    const isPetAdopted = Array.isArray(pet?.status) && pet.status.includes('ADOPTED');

    const showToast = useCallback((title, message, type = 'info', duration = 2600) => {
        setToast({ visible: true, title, message, type });
        setTimeout(() => {
            setToast((prev) => ({ ...prev, visible: false }));
        }, duration);
    }, []);

    const closeAll = useCallback(() => {
        setIsStatusModalVisible(false);
        setIsAdotanteModalVisible(false);
        setIsInteressadosModalVisible(false);
        setIsDeleteConfirmModalVisible(false);
        setInteressados([]);
        setIsLoadingInteressados(false);
        setAdotanteInfo(null);
        onClose();
    }, [onClose]);

    const handleOpenDeleteConfirm = () => {
        setIsDeleteConfirmModalVisible(true);
    };

    const handleDeletePet = async () => {
        if (!pet?.petId) {
            return;
        }

        try {
            await deletarPet(pet.petId);
            showToast('Sucesso', 'Pet deletado com sucesso.', 'success');
            closeAll();
            onRefresh();
        } catch (error) {
            showToast('Erro', 'Não foi possível deletar o pet.', 'error');
        }
    };

    const fetchAdotanteInfo = useCallback(async () => {
        if (!pet?.petId) {
            return null;
        }

        try {
            const res = await fetchAdotanteInfoPetCard(pet.petId);
            const info = res?.data || null;
            setAdotanteInfo(info);
            return info;
        } catch (error) {
            setAdotanteInfo(null);
            return null;
        }
    }, [pet?.petId]);

    const handleShowAdotanteModal = async () => {
        await fetchAdotanteInfo();
        setIsAdotanteModalVisible(true);
    };

    const handleVoltarParaAdocao = async () => {
        if (!pet?.petId) {
            return;
        }

        const info = adotanteInfo || (await fetchAdotanteInfo());
        const adotanteIdToDelete = info?.userId || ADOTANTE_EXTERNO_ID;

        try {
            await voltarParaAdocao(pet.petId, adotanteIdToDelete);
            showToast('Sucesso', 'Pet voltou para adoção.', 'success');
            closeAll();
            onRefresh();
        } catch (error) {
            showToast('Erro', 'Não foi possível voltar para adoção.', 'error');
        }
    };

    const handleMarcarComoAdotadoExterno = async () => {
        if (!pet?.petId) {
            return;
        }

        try {
            await marcarComoAdotadoExterno(pet.petId);
            showToast('Sucesso', 'Pet marcado como adotado por outra plataforma.', 'success');
            closeAll();
            onRefresh();
        } catch (error) {
            showToast('Erro', 'Não foi possível atualizar o status.', 'error');
        }
    };

    const handleMarcarComoAdotadoPeTinder = async () => {
        if (!pet?.petId) {
            return;
        }

        try {
            setIsLoadingInteressados(true);
            const session = await getSession();
            const ongId = session?.ongId;

            if (!ongId) {
                showToast('Erro', 'ONG não encontrada na sessão.', 'error');
                return;
            }

            const data = await fetchAdotantesInteressados(pet.petId);
            setInteressados(data);
            setIsStatusModalVisible(false);
            setIsInteressadosModalVisible(true);
        } catch (error) {
            showToast('Erro', 'Não foi possível buscar interessados.', 'error');
        } finally {
            setIsLoadingInteressados(false);
        }
    };

    const handleSelectInteressado = async (userId) => {
        if (!pet?.petId || !userId) {
            return;
        }

        try {
            await marcarPetComoAdotado(pet.petId, userId);
            showToast('Sucesso', 'Pet marcado como adotado pelo PeTinder.', 'success');
            closeAll();
            onRefresh();
        } catch (error) {
            showToast('Erro', 'Não foi possível marcar pet como adotado.', 'error');
        }
    };

    const handleGoToEdit = () => {
        if (!pet) {
            return;
        }

        router.push({
            pathname: '/ong/petForm',
            params: {
                mode: 'edit',
                petId: String(pet.petId),
                backTo: '/ong/pets',
                nome: pet.petNome || '',
                idade: pet.idade !== undefined && pet.idade !== null ? String(pet.idade) : '',
                porte: pet.porte || '',
                tags: (pet.tags || []).join('|'),
                descricao: pet.descricao || '',
                isCastrado: String(Boolean(pet.isCastrado)),
                isVermifugo: String(Boolean(pet.isVermifugo)),
                isVacinado: String(Boolean(pet.isVacinado)),
                sexo: pet.sexo || '',
                imageUrls: (pet.imageUrl || []).join('|'),
            },
        });

        closeAll();
    };

    return (
        <>
            <PetDetailsModal
                visible={visible}
                onClose={closeAll}
                onDelete={handleOpenDeleteConfirm}
                onEdit={handleGoToEdit}
                onOpenStatus={() => setIsStatusModalVisible(true)}
                pet={pet}
            />

            <GenericModal
                visible={isDeleteConfirmModalVisible}
                onClose={() => setIsDeleteConfirmModalVisible(false)}
                title="Confirmação"
            >
                <Text style={styles.confirmMessage}>
                    Tem certeza que deseja deletar o pet {pet?.petNome ? `"${pet.petNome}"` : ''}?
                </Text>

                <View style={styles.confirmActions}>
                    <DynamicButton
                        variant="modal-secondary"
                        onPress={() => setIsDeleteConfirmModalVisible(false)}
                        style={styles.confirmButton}
                        textStyle={{ color: '#80465D', fontSize: 16 }}
                    >
                        Cancelar
                    </DynamicButton>

                    <DynamicButton
                        variant="modal-primary"
                        onPress={handleDeletePet}
                        style={styles.confirmButton}
                        textStyle={{ color: '#fff', fontSize: 16 }}
                    >
                        Deletar
                    </DynamicButton>
                </View>
            </GenericModal>

            <AdoptionStatusModal
                visible={isStatusModalVisible}
                onClose={() => setIsStatusModalVisible(false)}
                isAdopted={isPetAdopted}
                onBackToAdoption={handleVoltarParaAdocao}
                onViewAdopter={handleShowAdotanteModal}
                onAdoptedByPetinder={handleMarcarComoAdotadoPeTinder}
                onAdoptedExternally={handleMarcarComoAdotadoExterno}
            />

            <AdopterInfoModal
                visible={isAdotanteModalVisible}
                onClose={() => setIsAdotanteModalVisible(false)}
                petName={pet?.petNome}
                adopterInfo={adotanteInfo}
            />

            <InterestedUsersModal
                visible={isInteressadosModalVisible}
                onClose={() => setIsInteressadosModalVisible(false)}
                petName={pet?.petNome}
                interestedUsers={interessados}
                isLoading={isLoadingInteressados}
                onSelectUser={handleSelectInteressado}
            />

            <Toast
                visible={toast.visible}
                title={toast.title}
                message={toast.message}
                type={toast.type}
            />
        </>
    );
}

const styles = StyleSheet.create({
    confirmMessage: {
        fontSize: 15,
        color: colors.textStrong,
        textAlign: 'center',
    },
    confirmActions: {
        flexDirection: 'row',
        justifyContent: 'center',
        gap: 10,
        marginTop: 8,
    },
    confirmButton: {
        width: '48%',
    },
});
