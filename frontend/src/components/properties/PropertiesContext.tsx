"use client"

import {Property} from "@/types/types";
import React, {Context, createContext, useContext, useState} from "react";

interface PropertiesContextType {
    properties: Property[];
    setProperties: React.Dispatch<React.SetStateAction<Property[]>>;
    addProperty: (property: Property) => void;
}

const PropertiesContext: Context<PropertiesContextType | undefined> = createContext<PropertiesContextType | undefined>(undefined);

export const useProperties = (): PropertiesContextType => {
    const context: PropertiesContextType | undefined = useContext(PropertiesContext);
    if (!context) {
        throw new Error("useProperties must be used within a PropertiesProvider");
    }
    return context;
};

export const PropertiesProvider: React.FC<{ children: React.ReactNode, initialProperties: Property[]; }> = ({ children, initialProperties=[] }) => {
    const [properties, setProperties] = useState<Property[]>(initialProperties);

    const addProperty = (property: Property): void => {
        setProperties((prevProperties: Property[]): Property[] => [...prevProperties, property]);
    };

    return (
        <PropertiesContext.Provider value={{ properties, setProperties, addProperty }}>
            {children}
        </PropertiesContext.Provider>
    );
};
