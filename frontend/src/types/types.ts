interface Property {
    id: number;
    name: string;
    type: string;
    price: number;
    address: string;
    image: string | undefined;
    description: string | undefined;
}

export type { Property };