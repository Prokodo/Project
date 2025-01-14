"use client"

import {getCookie} from "@/utils/cookies";
import React, {JSX, useState} from "react";
import {useRequests} from "@/components/requests/RequestsContext";

const UserRequestsList = (): JSX.Element => {
    const { requests } = useRequests();

    const [searchTerm, setSearchTerm] = useState<string>("");
    const [filterStatus, setFilterStatus] = useState<string | null>(null);

    const filteredRequests = requests.filter((request) => {
        const matchesSearchTerm =
            request.property.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
            request.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
            request.property.address.toLowerCase().includes(searchTerm.toLowerCase());

        const matchesStatus = !filterStatus || request.status === filterStatus;

        return matchesSearchTerm && matchesStatus;
    });

    const updateRequestStatus = async (propertyId: number, requestId: number, newStatus: string): Promise<void> => {
        try {
            const url: string = `http://localhost:8080/api/requests/status/${requestId}`;
            const authToken: string = getCookie("authToken") || "";
            await fetch(url, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
                body: JSON.stringify({
                    status: newStatus
                }),
            });


        } catch (error) {
            console.error("Failed to update request status:", error);
        }
    };

    return (
        <div>
            <div className="mt-8 mx-4 p-6 bg-white shadow-sm rounded-xl border border-gray-200">
                <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Request List</h1>

                <div className="mb-4">
                    <input type="text" placeholder="Search properties..." value={searchTerm}
                           onChange={(e): void => setSearchTerm(e.target.value)}
                           className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"/>
                </div>



                <div className="space-y-4">
                    {filteredRequests.map((request) => (
                        <div key={request.id} className="border border-gray-300 rounded-md shadow-sm py-3 px-4">
                            <div className="flex justify-between items-center">
                                <div>
                                    <h2 className="text-xl font-semibold text-gray-700">
                                        {request.description}
                                    </h2>
                                    <p className="text-sm text-gray-600">
                                        Address: {request.property.address}
                                    </p>
                                    <p className="text-sm text-gray-600">
                                        Rent: {request.property.price}Kč/month
                                    </p>
                                </div>

                                <div className="flex items-center">
                                    <span title={request.status}
                                          className={`w-3 h-3 mr-4 block rounded-full ${
                                              request.status === "REQUESTED" ? "bg-yellow-500" :
                                                  request.status === "IN_PROGRESS" ? "bg-blue-500" :
                                                      request.status === "COMPLETED" ? "bg-green-500" : "bg-red-500"
                                          }`} />
                                    <span className="text-gray-700 text-sm">{request.status}</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default UserRequestsList;
