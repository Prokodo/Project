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
}

export type { Property, Request, RequestStatus };