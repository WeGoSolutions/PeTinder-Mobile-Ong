import { StyleSheet, View } from 'react-native';
import GenericModal from '../GenericModal';
import DynamicButton from '../DynamicButton';

export default function AdoptionStatusModal({
    visible,
    onClose,
    isAdopted,
    onBackToAdoption,
    onViewAdopter,
    onAdoptedByPetinder,
    onAdoptedExternally,
}) {
    return (
        <GenericModal
            visible={visible}
            onClose={onClose}
            title="Status de adoção"
        >
            {isAdopted ? (
                <View style={styles.statusActionsColumn}>
                    <DynamicButton variant="primary" textStyle={{ color: 'white' }} onPress={onBackToAdoption}>
                        Voltar para adoção
                    </DynamicButton>
                    <DynamicButton variant="secondary" textStyle={{ color: '#80465D' }} onPress={onViewAdopter}>
                        Ver adotante
                    </DynamicButton>
                </View>
            ) : (
                <View style={styles.statusActionsColumn}>
                    <DynamicButton variant="primary" textStyle={{ color: 'white' }} onPress={onAdoptedByPetinder}>
                        Adotado pelo PeTinder
                    </DynamicButton>
                    <DynamicButton variant="secondary" textStyle={{ color: '#80465D' }} onPress={onAdoptedExternally}>
                        Adotado por outra plataforma
                    </DynamicButton>
                </View>
            )}
        </GenericModal>
    );
}

const styles = StyleSheet.create({
    statusActionsColumn: {
        gap: 10,
    },
});
