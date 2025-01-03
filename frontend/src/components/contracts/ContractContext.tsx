"use client"

import {Contract} from "@/types/types";
import React, {Context, createContext, useContext, useState} from "react";

interface ContractContextType {
    contracts: Contract[];
    addContract: (contract: Contract) => void;
    editContract: (updatedContract: Contract) => void;
    setContracts: React.Dispatch<React.SetStateAction<Contract[]>>;
}

const ContractContext: Context<ContractContextType | undefined> = createContext<ContractContextType | undefined>(undefined);

export const useContracts = (): ContractContextType => {
    const context: ContractContextType | undefined = useContext(ContractContext);
    if (!context) {
        throw new Error("useContracts must be used within a ContractProvider");
    }
    return context;
};

export const ContractProvider: React.FC<{ children: React.ReactNode, initialContracts: Contract[]; }> = ({ children, initialContracts=[] }) => {
    const [contracts, setContracts] = useState<Contract[]>(initialContracts);

    const addContract = (contract: Contract): void => {
        setContracts((prevContracts: Contract[]): Contract[] => [...prevContracts, contract]);
    };

    const editContract = (updatedContract: Contract): void => {
        setContracts((prevContracts: Contract[]): Contract[] =>
            prevContracts.map((contract: Contract): Contract =>
                contract.id === updatedContract.id ? updatedContract : contract
            )
        );
    };

    return (
        <ContractContext.Provider value={{ contracts, setContracts, editContract, addContract }}>
            {children}
        </ContractContext.Provider>
    );
};
