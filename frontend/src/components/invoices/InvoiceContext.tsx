"use client"

import {Invoice} from "@/types/types";
import React, {Context, createContext, useContext, useState} from "react";

interface InvoiceContextType {
    invoices: Invoice[];
    setInvoices: React.Dispatch<React.SetStateAction<Invoice[]>>;

    addInvoice: (invoice: Invoice) => void;
    updateInvoiceStatus: (id: number, status: string) => void;
}

const InvoiceContext: Context<InvoiceContextType | undefined> = createContext<InvoiceContextType | undefined>(undefined);

export const useInvoices = (): InvoiceContextType => {
    const context: InvoiceContextType | undefined = useContext(InvoiceContext);
    if (!context) {
        throw new Error("useProperties must be used within a PropertiesProvider");
    }
    return context;
};

export const InvoiceProvider: React.FC<{ children: React.ReactNode, initialInvoices: Invoice[]; }> = ({ children, initialInvoices=[] }) => {
    const [invoices, setInvoices] = useState<Invoice[]>(initialInvoices);

    const addInvoice = (invoice: Invoice): void => {
        setInvoices((prevInvoices: Invoice[]): Invoice[] => [...prevInvoices, invoice]);
    };

    const updateInvoiceStatus = (id: number, status: string): void => {
        setInvoices((prevInvoices: Invoice[]) =>
            prevInvoices.map((invoice: Invoice) =>
                invoice.id === id ? { ...invoice, status } : invoice
            )
        );
    };

    return (
        <InvoiceContext.Provider value={{ invoices, setInvoices, addInvoice, updateInvoiceStatus }}>
            {children}
        </InvoiceContext.Provider>
    );
};
