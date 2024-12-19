"use client"

import {useState} from "react";
import {Property} from "@/types/types";
import {DataTable} from "@/components/properties/DataTable";

const PropertiesList = ({ listOfProperties }: { listOfProperties: Property[] }) => {
    const [properties, _] = useState(listOfProperties);
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
            accessorKey: "propertyValue",
            header: "Value",
        },
    ];

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Properties List</h1>
            <DataTable
                columns={columns}
                data={listOfProperties}
                options={{
                    sorting: true,
                    filtering: true,
                    pagination: true,
                }}
            />
        </div>
    );
};

export default PropertiesList;
