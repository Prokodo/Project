import {ReactElement} from "react";
import {fetchProperties} from "@/services/properties";
import {AuthorityResponse, Property} from "@/types/types";
import {checkUserAuthority, getAuthToken} from "@/utils/auth";
import RequestsList from "@/components/requests/RequestsList";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const response: AuthorityResponse = await checkUserAuthority(authToken, "ROLE_ADMIN");

    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <PropertiesProvider initialProperties={properties}>
            {properties.length > 0 && <RequestsList roles={response.roles} />}
        </PropertiesProvider>
    );
}