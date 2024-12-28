"use client"

import {Property, Request} from "@/types/types";
import React, {Context, createContext, useContext, useState} from "react";

interface PropertiesContextType {
    properties: Property[];
    setProperties: React.Dispatch<React.SetStateAction<Property[]>>;

    addProperty: (property: Property) => void;
    editProperty: (updatedProperty: Property) => void;

    addRequest: (propertyId: number, request: Request) => void;
    editRequest: (propertyId: number, updatedRequest: Request) => void;
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

    const editProperty = (updatedProperty: Property): void => {
        setProperties((prevProperties: Property[]): Property[] =>
            prevProperties.map((property: Property): Property =>
                property.id === updatedProperty.id ? updatedProperty : property
            )
        );
    };

    const addRequest = (propertyId: number, request: Request): void => {
        setProperties((prevProperties: Property[]): Property[] =>
            prevProperties.map((property: Property): Property =>
                property.id === propertyId ? {
                    ...property,
                    requests: property.requests ? [...property.requests, request] : [request],
                }
                : property
            )
        );
    };

    const editRequest = (propertyId: number, updatedRequest: Request): void => {
        setProperties((prevProperties: Property[]): Property[] =>
            prevProperties.map((property: Property): Property =>
                property.id === propertyId
                    ? {
                        ...property,
                        requests: property.requests
                            ? property.requests.map((request: Request): Request =>
                                request.id === updatedRequest.id ? updatedRequest : request
                            )
                            : [],
                    }
                    : property
            )
        );
    };

    return (
        <PropertiesContext.Provider value={{
            properties, setProperties,
            addProperty, editProperty,
            addRequest, editRequest
        }}>
            {children}
        </PropertiesContext.Provider>
    );
};
