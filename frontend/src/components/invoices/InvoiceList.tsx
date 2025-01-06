"use client"

import React, {useState} from "react";
import {Property} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import {DataTable} from "@/components/common/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import PropertiesForm from "@/components/properties/PropertiesForm";
import {useProperties} from "@/components/properties/PropertiesContext";
import {useInvoices} from "@/components/invoices/InvoiceContext";

const InvoiceList = () => {
    const { invoices } = useInvoices();
    const [filterStatus, setFilterStatus] = useState<string | null>(null);

    const handleFilterChange = (status: string | null) => {
        setFilterStatus(status);
    };

    const filteredInvoices = React.useMemo(() => {
        return invoices.filter((invoice) => {
            if (filterStatus === null) return true;
            return filterStatus === "Paid" ? invoice.paid : !invoice.paid;
        });
    }, [filterStatus, invoices]);

    const columns = [
        {
            accessorKey: "issueDate",
            header: "Issue date",
        },
        {
            accessorKey: "dueDate",
            header: "Due date",
        },
        {
            accessorKey: "amount", header: "Amount to pay",
            cell: ({ row }: { row: { original: { amount: number, paid: boolean } } }) => (
                <div>
                    {row.original.paid? 0 : row.original.amount} kč
                </div>
            ),
        },
        {
            accessorKey: "paid", header: "Status",
            cell: ({ row }: { row: { original: { paid: boolean } } }) => (
                <div className="flex items-center">
                    {!row.original.paid ? (
                        <div className="flex items-center space-x-2">
                            <div className="w-4 h-4 bg-red-500 rounded-full" title="Unpaid" />
                            <span className="text-red-600">Unpaid</span>
                        </div>
                    ) : (
                        <div className="flex items-center space-x-2">
                            <div className="w-4 h-4 bg-green-500 rounded-full" title="Paid" />
                            <span className="text-green-600">Paid</span>
                        </div>
                    )}
                </div>
            ),
        },
    ];

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Invoices List</h1>

            <div className="flex justify-end mb-4">
                <select value={filterStatus || ""} onChange={(e) => handleFilterChange(e.target.value === "" ? null : e.target.value)}
                        className="border border-gray-300 rounded-md px-3 py-1">
                    <option value="">All</option>
                    <option value="Paid">Paid</option>
                    <option value="Unpaid">Unpaid</option>
                </select>
            </div>
            <DataTable key={filterStatus} columns={columns} data={filteredInvoices}/>
        </div>
    );
};

export default InvoiceList;
