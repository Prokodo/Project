"use client"

import {Property, Tenant} from "@/types/types";
import React, {Context, createContext, useContext, useState} from "react";

interface TenantsContextType {
    tenants: Tenant[];
    addTenant: (tenant: Tenant) => void;
    editTenant: (updatedTenant: Tenant) => void;
    setTenants: React.Dispatch<React.SetStateAction<Tenant[]>>;
}

const TenantContext: Context<TenantsContextType | undefined> = createContext<TenantsContextType | undefined>(undefined);

export const useTenants = (): TenantsContextType => {
    const context: TenantsContextType | undefined = useContext(TenantContext);
    if (!context) {
        throw new Error("useTenants must be used within a TenantProvider");
    }
    return context;
};

export const TenantsProvider: React.FC<{ children: React.ReactNode, initialTenants: Tenant[]; }> = ({ children, initialTenants=[] }) => {
    const [tenants, setTenants] = useState<Tenant[]>(initialTenants);

    const addTenant = (tenant: Tenant): void => {
        setTenants((prevTenants: Tenant[]): Tenant[] => [...prevTenants, tenant]);
    };

    const editTenant = (updatedTenant: Tenant): void => {
        setTenants((prevTenants: Tenant[]): Tenant[] =>
            prevTenants.map((tenant: Tenant): Tenant =>
                tenant.id === updatedTenant.id ? updatedTenant : tenant
            )
        );
    };

    return (
        <TenantContext.Provider value={{ tenants, setTenants, editTenant, addTenant }}>
            {children}
        </TenantContext.Provider>
    );
};
