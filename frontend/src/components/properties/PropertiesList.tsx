"use client"

import {useState} from "react";
import {Property} from "@/types/types";
import {DataTable} from "@/components/properties/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import {useProperties} from "@/components/properties/PropertiesContext";

const PropertiesList = () => {
    const { properties, setProperties } = useProperties();

    const [error, setError] = useState<string | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [propertyToDelete, setPropertyToDelete] = useState<Property | null>(null);

    const handleDelete = async (): Promise<void> => {
        if (propertyToDelete) {
            try {
                const response = await fetch(`http://localhost:8080/api/properties/${propertyToDelete.id}`, {
                    method: "DELETE",
                });

                if (response.ok) {
                    const updatedProperties: Property[] = properties.filter(
                        (property: Property): boolean => property.id !== propertyToDelete.id
                    );
                    setProperties(updatedProperties);
                } else {
                    const errorText = `Failed to delete property: ${response.status} ${response.statusText}`;
                    setError(errorText);
                }
            } catch (error: any) {
                setError(`Error deleting property: ${error.message}`);
            } finally {
                setPropertyToDelete(null);
            }
        }
        setIsDialogOpen(false);
    };

    const openDeleteDialog = (property: Property): void => {
        setPropertyToDelete(property);
        setIsDialogOpen(true);
    };

    const columns = [
        {
            accessorKey: "name",
            header: "Name",
        },
        {
            accessorKey: "address",
            header: "Address",
        },
        {
            accessorKey: "type",
            header: "Type",
        },
        {
            accessorKey: "description",
            header: "Description",
        },
        {
            accessorKey: "propertyValue",
            header: "Value",
        },
        {
            id: "actions",
            header: "Actions",
            cell: ({ row }: { row: { original: Property } }) => (
                <ConfirmDialog
                    title="Are you sure?"
                    description="This action cannot be undone. It will permanently delete the property."
                    confirmLabel="Delete"
                    cancelLabel="Cancel"
                    isOpen={isDialogOpen}
                    onConfirm={handleDelete}
                    onCancel={(): void => setIsDialogOpen(false)}
                    trigger={
                        <button onClick={(): void => openDeleteDialog(row.original)} className="text-red-600 hover:text-red-800">
                            Delete
                        </button>
                    }
                />
            ),
        },
    ];

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Properties List</h1>
            {error && (
                <div className="mb-4 p-3 text-red-800 bg-red-100 border border-red-300 rounded">
                    {error}
                </div>
            )}

            <DataTable columns={columns} data={properties} options={{
                sorting: true,
                filtering: true,
                pagination: true,
            }} />
        </div>
    );
};

export default PropertiesList;
