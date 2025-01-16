import React from "react";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription,
    AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger,
} from "@/components/ui/alert-dialog"

interface DialogProps {
    title: string;
    description: string;
    confirmLabel: string;
    cancelLabel: string;
    isOpen: boolean;
    onConfirm: () => void;
    onCancel: () => void;
    trigger?: React.ReactNode;
}

const ConfirmDialog: React.FC<DialogProps> = ({
    title, description, confirmLabel, cancelLabel, isOpen,
    onConfirm, onCancel, trigger
}: DialogProps) => {
    return (
        <AlertDialog open={isOpen}     onOpenChange={(open) => {
            if (!open) {
                onCancel();
            }
        }}>
            <AlertDialogTrigger asChild>{trigger}</AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogHeader className="flex justify-between items-start">
                    <div>
                        <AlertDialogTitle>{title}</AlertDialogTitle>
                        <AlertDialogDescription>{description}</AlertDialogDescription>
                    </div>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel asChild>
                        <button onClick={onCancel} className="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300">
                            {cancelLabel}
                        </button>
                    </AlertDialogCancel>
                    <AlertDialogAction asChild>
                        <button onClick={onConfirm} className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700">
                            {confirmLabel}
                        </button>
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    );
};

export default ConfirmDialog;