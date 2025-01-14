"use client"

import {Request} from "@/types/types";
import React, {Context, createContext, useContext, useState} from "react";

interface RequestsContextType {
    requests: Request[];
    addRequest: (request: Request) => void;
    setRequests: React.Dispatch<React.SetStateAction<Request[]>>;
}

const RequestContext: Context<RequestsContextType | undefined> = createContext<RequestsContextType | undefined>(undefined);

export const useRequests = (): RequestsContextType => {
    const context: RequestsContextType | undefined = useContext(RequestContext);
    if (!context) {
        throw new Error("useRequests must be used within a PropertiesProvider");
    }
    return context;
};

export const RequestProvider: React.FC<{ children: React.ReactNode, initialRequests: Request[]; }> = ({ children, initialRequests=[] }) => {
    const [requests, setRequests] = useState<Request[]>(initialRequests);

    const addRequest = (request: Request): void => {
        setRequests((prevRequest: Request[]): Request[] => [...prevRequest, request]);
    };

    return (
        <RequestContext.Provider value={{ requests, addRequest, setRequests }}>
            {children}
        </RequestContext.Provider>
    );
};
