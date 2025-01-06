import {ReactElement} from "react";
import {fetchTenants} from "@/services/tenants";
import {fetchContracts} from "@/services/contracts";
import {fetchProperties} from "@/services/properties";
import {Contract, Property, RolesResponse, Tenant} from "@/types/types";
import {checkUserAuthority, getAuthToken} from "@/utils/auth";
import ContractList from "@/components/contracts/ContractList";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import {ContractProvider} from "@/components/contracts/ContractContext";
import ContractPopupForm from "@/components/contracts/ContractPopupForm";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import {getUserRoles} from "@/services/global";
import {redirect} from "next/navigation";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }

    const isAdmin: boolean = roles.roles.includes("ROLE_ADMIN");
    const fetchData = {
        contracts: (): Promise<Contract[] | undefined> => fetchContracts(authToken),
        tenants: isAdmin ? (): Promise<Tenant[] | undefined> => fetchTenants(authToken) : (): Promise<Tenant[]> => Promise.resolve([] as Tenant[]),
        properties: isAdmin ? (): Promise<Property[] | undefined> => fetchProperties(authToken) : (): Promise<Property[]> => Promise.resolve([] as Property[])
    };

    const [tenants, contracts, properties] = await Promise.all([
        fetchData.tenants().catch((): Tenant[] => [] as Tenant[]),
        fetchData.contracts().catch((): Contract[] => [] as Contract[]),
        fetchData.properties().catch((): Property[] => [] as Property[])
    ]);

    return (
        <PropertiesProvider initialProperties={properties || []}>
            <TenantsProvider initialTenants={tenants || []}>
                <ContractProvider initialContracts={contracts || []}>
                    {roles.roles.includes("ROLE_ADMIN") && <ContractPopupForm />}
                    <ContractList roles={roles.roles} />
                </ContractProvider>
            </TenantsProvider>
        </PropertiesProvider>
    );
}