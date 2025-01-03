import {ReactElement} from "react";
import {Contract, Property, Tenant} from "@/types/types";
import {fetchProperties} from "@/services/properties";
import {checkUserAuthority, getAuthToken} from "@/utils/auth";

import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import ContractForm from "@/components/contracts/ContractsForm";
import {fetchTenants} from "@/services/tenants";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import ContractList from "@/components/contracts/ContractList";
import {fetchContracts} from "@/services/contracts";
import {ContractProvider} from "@/components/contracts/ContractContext";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    await checkUserAuthority(authToken, "ROLE_ADMIN");

    const tenants: Tenant[] = await fetchTenants(authToken) || [];
    const contracts: Contract[] = await fetchContracts(authToken) || [];
    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <PropertiesProvider initialProperties={properties}>
            <TenantsProvider initialTenants={tenants}>
                <ContractProvider initialContracts={contracts}>
                    <ContractForm />
                    <ContractList />
                </ContractProvider>
            </TenantsProvider>
        </PropertiesProvider>
    );
}