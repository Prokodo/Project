import {ReactElement} from "react";
import {redirect} from "next/navigation";
import {getAuthToken} from "@/utils/auth";
import {getUserRoles} from "@/services/global";
import {fetchProperties} from "@/services/properties";
import {Property, RolesResponse} from "@/types/types";
import RequestsList from "@/components/requests/RequestsList";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }

    const isAdmin: boolean = roles.roles.includes("ROLE_ADMIN");
    if (!isAdmin) {
        return (
            <>
            </>
        )
    }

    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <PropertiesProvider initialProperties={properties}>
            <RequestsList roles={roles.roles} />
        </PropertiesProvider>
    );
}