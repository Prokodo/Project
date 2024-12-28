import {ReactElement} from "react";
import {cookies} from "next/headers";
import {Property} from "@/types/types";
import {redirect} from "next/navigation";
import {fetchProperties} from "@/services/properties";
import RequestsList from "@/components/requests/RequestsList";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import {ReadonlyRequestCookies} from "next/dist/server/web/spec-extension/adapters/request-cookies";

export default async function PropertiesPage(): Promise<ReactElement> {
    const cookieStore: ReadonlyRequestCookies = await cookies();
    const authToken: string | undefined = cookieStore.get("authToken")?.value;
    if (!authToken) {
        redirect('/login');
    }

    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <PropertiesProvider initialProperties={properties}>
            {properties.length > 0 && <RequestsList />}
        </PropertiesProvider>
    );
}