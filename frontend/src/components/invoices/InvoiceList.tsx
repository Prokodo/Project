"use client"

import "jspdf-autotable";
import jsPDF from "jspdf";
import React, {useState} from "react";
import {Invoice} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import {validRoles} from "@/services/global";
import {DataTable} from "@/components/common/DataTable";
import {useInvoices} from "@/components/invoices/InvoiceContext";

const InvoiceList = ({ roles = [] }: { roles: validRoles[] }) => {
    const { invoices, updateInvoiceStatus } = useInvoices();
    const [filterStatus, setFilterStatus] = useState<string | null>(null);
    const [generalError, setGeneralError] = useState<string | null>(null);

    const handleFilterChange = (status: string | null): void => {
        setFilterStatus(status);
    };

    const handleStatusChange = async (id: number, currentStatus: string): Promise<void> => {
        try {
            const url = `http://localhost:8080/api/invoices/invoice-status/${id}`;
            const authToken: string = getCookie("authToken") || "";

            const response = await fetch(url, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
                body: JSON.stringify({
                    status: currentStatus,
                }),
            });

            if (response.ok) {
                updateInvoiceStatus(id, currentStatus);
            } else {
                const errorData = await response.json();
                setGeneralError(errorData.message || "Failed to update invoice status.");
            }
        } catch (error) {
            setGeneralError("An unexpected error occurred. Please try again.");
        }
    };

    const filteredInvoices = React.useMemo((): Invoice[] => {
        return invoices.filter((invoice: Invoice): boolean => {
            if (filterStatus === null) return true;
            return invoice.status === filterStatus;
        });
    }, [filterStatus, invoices]);

    const generateInvoicePDF = (invoice: Invoice): void => {
        const doc = new jsPDF();

        doc.setFontSize(18);
        doc.setTextColor(40);
        doc.text("Invoice", 105, 15, { align: "center" });

        doc.setFontSize(12);
        doc.setTextColor(70);
        doc.text(`Invoice ID: ${invoice.id}`, 14, 30);
        doc.text(`Tenant Name: ${invoice.contract.tenant.firstName} ${invoice.contract.tenant.surname}`, 14, 40);
        doc.text(`Email: ${invoice.contract.tenant.email}`, 14, 50);
        doc.text(`Phone: ${invoice.contract.tenant.phoneNumber}`, 14, 60);

        doc.text(`Property Name: ${invoice.contract.property.name}`, 14, 70);
        doc.text(`Amount: ${invoice.amount} kč`, 14, 80);
        doc.text(`Due Date: ${invoice.dueDate}`, 14, 90);
        doc.text(`Status: ${invoice.status}`, 14, 100);

        const tableData: string[][] = [
            ["Description", "Value"],
            ["Tenant Name", `${invoice.contract.tenant.firstName} ${invoice.contract.tenant.surname}`],
            ["Property", invoice.contract.property.name],
            ["Amount", `${invoice.amount} kč`],
            ["Due Date", invoice.dueDate],
            ["Status", invoice.status],
        ];

        // @ts-ignore
        doc.autoTable({
            startY: 110,
            head: [["Field", "Details"]],
            body: tableData,
            theme: "grid",
            headStyles: { fillColor: [41, 128, 185] },
            styles: { fontSize: 10, cellPadding: 4 },
        });

        doc.setFontSize(10);
        doc.setTextColor(150);
        doc.text("This is a computer-generated invoice.", 105, doc.internal.pageSize.height - 10, { align: "center" });

        doc.save(`invoice-${invoice.id}.pdf`);
    };

    const columns = [
        {
            accessorKey: "issueDate",
            header: "Issue date",
        },
        {
            accessorKey: "contract.tenant", header: "Tenant Name",
            cell: ({ row }: { row: { original: { contract: { tenant: { firstName: string; surname: string } } } } }) => (
                <div>
                    {row.original.contract.tenant.firstName} {row.original.contract.tenant.surname}
                </div>
            ),
        },
        {
            accessorKey: "contract.tenant.email",
            header: "Email",
        },
        {
            accessorKey: "contract.tenant.phoneNumber",
            header: "Phone number",
        },
        {
            accessorKey: "contract.property", header: "Property Name",
            cell: ({ row }: { row: { original: { contract: { property: { name: string } } } } }) => (
                <div>{row.original.contract.property.name}</div>
            ),
        },
        {
            accessorKey: "amount", header: "Amount to pay",
            cell: ({ row }: { row: { original: { amount: number, status: string } } }) => (
                <div style={{ color: row.original.status === "PAID" ? "green" : "red", fontWeight: "bold" }}>
                    {row.original.status === "PAID" ? 0 : row.original.amount} kč
                </div>
            ),
        },
        {
            accessorKey: "dueDate",
            header: "Due date",
        },
        {
            accessorKey: "status", header: "Status",
            cell: ({ row }: { row: { original: { id: number; status: string; } } }) => (
                <div className="flex items-center">
                    {!roles.includes("ROLE_ADMIN") ? (
                        <div className="flex items-center space-x-2">
                            {row.original.status === "PAID" ? (
                                <>
                                    <div className="w-4 h-4 bg-green-500 rounded-full" title="Paid" />
                                    <span className="text-green-600">Paid</span>
                                </>
                            ) : row.original.status === "UNPAID" ? (
                                <>
                                    <div className="w-4 h-4 bg-red-500 rounded-full" title="Unpaid" />
                                    <span className="text-red-600">Unpaid</span>
                                </>
                            ) : row.original.status === "PENDING" ? (
                                <>
                                    <div className="w-4 h-4 bg-yellow-500 rounded-full" title="Pending" />
                                    <span className="text-yellow-600">Pending</span>
                                </>
                            ) : row.original.status === "OVERDUE" ? (
                                <>
                                    <div className="w-4 h-4 bg-orange-500 rounded-full" title="Overdue" />
                                    <span className="text-orange-600">Overdue</span>
                                </>
                            ) : (
                                <>
                                    <div className="w-4 h-4 bg-gray-500 rounded-full" title="Cancelled" />
                                    <span className="text-gray-600">Cancelled</span>
                                </>
                            )}
                        </div>
                    ) : (
                        <select value={row.original.status} className="border border-gray-300 rounded-md px-2 py-1"
                                onChange={(e) => handleStatusChange(row.original.id, e.target.value)}>
                            <option value="PAID">Paid</option>
                            <option value="UNPAID">Unpaid</option>
                            <option value="PENDING">Pending</option>
                            <option value="OVERDUE">Overdue</option>
                            <option value="CANCELLED">Cancelled</option>
                        </select>
                    )}
                </div>
            ),
        },
        {
            header: "Actions", accessorKey: "actions",
            cell: ({ row }: { row: { original: Invoice }}) => (
                <button onClick={() => generateInvoicePDF(row.original)} className="bg-blue-600 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition">
                    Generate PDF
                </button>
            ),
        },
    ];

    return (
        <div className="mt-4 mx-4 p-6 bg-white shadow-sm rounded-xl border border-gray-200">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Invoices List</h1>

            {generalError && (
                <div className="mb-4 text-red-600">
                    {generalError}
                </div>
            )}

            <div className="flex justify-end mb-4">
                <select value={filterStatus || ""} className="border border-gray-300 rounded-md px-3 py-1"
                        onChange={(e): void => handleFilterChange(e.target.value === "" ? null : e.target.value)}>
                    <option value="">All</option>
                    <option value="PAID">Paid</option>
                    <option value="UNPAID">Unpaid</option>
                    <option value="PENDING">Pending</option>
                    <option value="OVERDUE">Overdue</option>
                    <option value="CANCELLED">Cancelled</option>
                </select>
            </div>
            <DataTable key={filterStatus} columns={columns} data={filteredInvoices}/>
        </div>
    );
};

export default InvoiceList;
