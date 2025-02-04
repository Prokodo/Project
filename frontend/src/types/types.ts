import {ValidRoles} from "@/services/global";

interface Property {
    id: number;
    name: string;
    type: string;
    price: number;
    address: string;
    image: string | undefined;
    description: string | undefined;
}

type RequestStatus = "REQUESTED" | "IN_PROGRESS" | "COMPLETED" | "REJECTED";

interface Request {
    id: number;
    description: string;
    status: RequestStatus;
    requestDate: string;
    completionDate?: string;
    property: Property;
}

type RolesResponse = {
    roles: ValidRoles[];
    loggedIn: boolean;
};

type AuthorityResponse = {
    authorized: boolean;
    message: string;
    roles: ValidRoles[];
};

interface Tenant {
    id: number;
    email: string;
    username: string;
    surname: string;
    firstName: string;
    phoneNumber: string;
    role?: "MANAGER" | "TENANT";
}

interface Contract {
    id: number;
    startDate: string;
    endDate: string;
    monthlyRent: number;
    tenant: Tenant;
    property: Property;
}

interface Invoice {
    id: number;
    contractId: number;
    issueDate: string;
    dueDate: string;
    amount: number;
    status: string;
    contract: Contract;
}

export type { Property, Request, RequestStatus, AuthorityResponse, RolesResponse, Tenant, Contract, Invoice };