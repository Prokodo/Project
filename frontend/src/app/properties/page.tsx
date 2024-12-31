import {ReactElement} from "react";
import {Property} from "@/types/types";
import {fetchProperties} from "@/services/properties";
import {checkUserAuthority, getAuthToken} from "@/utils/auth";
import PropertiesList from "@/components/properties/PropertiesList";
import PropertyPopupForm from "@/components/properties/PropertyPopupForm";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    await checkUserAuthority(authToken, "ROLE_ADMIN");

    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <PropertiesProvider initialProperties={properties}>
            <PropertyPopupForm />
            {properties.length > 0 && <PropertiesList />}
        </PropertiesProvider>
    );
}
