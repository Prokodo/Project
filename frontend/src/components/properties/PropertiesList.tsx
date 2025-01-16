"use client"

import React, {useMemo, useState} from "react";
import {Property} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import {DataTable} from "@/components/common/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import PropertiesForm from "@/components/properties/PropertiesForm";
import {useProperties} from "@/components/properties/PropertiesContext";

const PropertiesList = () => {
    const { properties, setProperties } = useProperties();

    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>("");
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);
    const [propertyToEdit, setPropertyToEdit] = useState<Property | null>(null);

    const openDeleteDialog = (property: Property): void => {
        setPropertyToEdit(property);
        setIsDialogOpen(true);
    };

    const openEditDialog = (property: Property): void => {
        setPropertyToEdit(property);
        setIsEditDialogOpen(true);
    };

    const handleDelete = async (): Promise<void> => {
        if (propertyToEdit) {
            try {
                const url: string = `http://localhost:8080/api/properties/${propertyToEdit.id}`;
                const authToken: string = getCookie("authToken") || "";
                const response = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                    cache: "no-store",
                });

                if (response.ok) {
                    const updatedProperties: Property[] = properties.filter(
                        (property: Property): boolean => property.id !== propertyToEdit.id
                    );
                    setProperties(updatedProperties);
                } else {
                    const errorText = `Failed to delete property: ${response.status} ${response.statusText}`;
                    setError(errorText);
                }
            } catch (error: any) {
                setError(`Error deleting property: ${error.message}`);
            } finally {
                setPropertyToEdit(null);
            }
        }
        setIsDialogOpen(false);
    };

    const filteredProperties: Property[] = useMemo((): Property[] => {
        const searchWords: string[] = searchTerm.toLowerCase().split(" ").filter((word: string): boolean => word.trim() !== "");
        return properties.filter((property: Property): boolean => {
            return searchWords.every((word: string): boolean => {
                return (
                    property.name.toLowerCase().includes(word) ||
                    property.address.toLowerCase().includes(word) ||
                    property.type.toLowerCase().includes(word) ||
                    property.description?.toLowerCase().includes(word) ||
                    property.price.toString().includes(word)
                );
            });
        });
    }, [properties, searchTerm]);

    const columns = [
        {
            accessorKey: "imageBytes",
            header: "Image",
            cell: ({ row }: { row: { original: Property } }) => {
                if (!row.original.image) {
                    return (
                      <div>No image found</div>
                    );
                }

                return (
                    <img className="w-20 h-20 object-cover rounded-md"
                         alt={row.original.name} title={row.original.name}
                         src={`data:image/jpeg;base64,${row.original.image}`} />
                );
            },
        },
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
            accessorKey: "price",
            header: "Price per month",
            cell: ({ row } : { row: { original: { price: number } } }): string => {
                const price: number = row.original.price;
                return `${price} kč`;
            }
        },
        {
            id: "actions",
            header: "Actions",
            cell: ({ row }: { row: { original: Property } }) => (
                <div className="flex space-x-4">
                    <button onClick={(): void => openEditDialog(row.original)} className="text-blue-600 hover:text-blue-800">
                        Edit
                    </button>
                    <button onClick={(): void => openDeleteDialog(row.original)} className="text-red-600 hover:text-red-800">
                        Delete
                    </button>
                </div>
            ),
        },
    ];

    return (
        <div className="mt-8 mx-4 p-6 bg-white shadow-sm rounded-xl border border-gray-200">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Properties list</h1>
            {error && (
                <div className="mb-4 p-3 text-red-800 bg-red-100 border border-red-300 rounded">
                    {error}
                </div>
            )}

            <div className="mb-4">
                <input type="text" placeholder="Search properties..." value={searchTerm}
                       onChange={(e): void => setSearchTerm(e.target.value)}
                       className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"/>
            </div>

            <ConfirmDialog title="Are you sure?"
                           confirmLabel="Delete"
                           cancelLabel="Cancel"
                           description="This action cannot be undone. It will permanently delete the property."
                           isOpen={isDialogOpen}
                           onConfirm={handleDelete}
                           onCancel={(): void => setIsDialogOpen(false)}/>

            {isEditDialogOpen && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl p-6 relative">
                        <button onClick={() => setIsEditDialogOpen(false)}
                                className="absolute top-3 right-3 text-gray-500 hover:text-gray-700 focus:outline-none">
                            ✖
                        </button>
                        <h2 className="text-2xl font-bold text-center mb-6">Edit property</h2>
                        <PropertiesForm setIsOpen={setIsEditDialogOpen} propertyToEdit={propertyToEdit || undefined}/>
                    </div>
                </div>
            )}

            <div className="max-h-[710px] overflow-y-auto">
                <DataTable columns={columns} data={filteredProperties}/>
            </div>
        </div>
    );
};

export default PropertiesList;
