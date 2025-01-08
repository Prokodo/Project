import {ReactElement} from "react";
import {Property, RolesResponse} from "@/types/types";
import {fetchProperties} from "@/services/properties";
import {getAuthToken} from "@/utils/auth";
import PropertiesList from "@/components/properties/PropertiesList";
import PropertyPopupForm from "@/components/properties/PropertyPopupForm";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import {getUserRoles} from "@/services/global";
import {redirect, unauthorized} from "next/navigation";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }
    if (!roles.roles.includes("ROLE_ADMIN")) {
        unauthorized();
    }

    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <PropertiesProvider initialProperties={properties}>
            <PropertyPopupForm />
            {properties.length > 0 && <PropertiesList />}
        </PropertiesProvider>
    );
}
