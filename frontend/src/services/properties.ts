import {Property} from "@/types/types";

async function fetchProperties(): Promise<Property[] | undefined> {
    try {
        const response: Response = await fetch("http://localhost:8080/api/properties");
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(error);
    }
}

export { fetchProperties };