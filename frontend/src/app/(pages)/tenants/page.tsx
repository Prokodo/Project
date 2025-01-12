import {ReactElement} from "react";
import {fetchTenants} from "@/services/tenants";
import {RolesResponse, Tenant} from "@/types/types";
import {getAuthToken} from "@/utils/auth";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import TenantsList from "@/components/tenants/TenantList";
import TenantsPopupForm from "@/components/tenants/TenantsPopupForm";
import {getUserRoles} from "@/services/global";
import {redirect, unauthorized} from "next/navigation";
import {HelpCircleIcon} from "lucide-react";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }
    if (!roles.roles.includes("ROLE_ADMIN")) {
        unauthorized();
    }

    const tenants: Tenant[] = await fetchTenants(authToken) || [];
    return (
        <main className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white py-4 shadow-md">
                <div className="mx-auto px-10 flex items-center justify-between">
                    <h1 className="text-2xl font-bold">Users management</h1>

                    <div className="relative group">
                        <HelpCircleIcon className="h-6 w-6 cursor-pointer"/>
                        <div
                            className="absolute top-full mt-2 w-max max-w-xs text-sm text-white bg-gray-800 rounded-md py-2 px-4 opacity-0 group-hover:opacity-100 transition-opacity duration-200 shadow-lg"
                            style={{
                                right: "0",
                                whiteSpace: "normal",
                                overflowWrap: "break-word",
                                transform: "translateX(0)",
                            }}>
                            Tip: Need Assistance? <br/>
                            If you encounter any issues with invoices or the application, please reach out to the
                            administrator responsible for your account.
                            They can assist you with troubleshooting and resolving your concerns promptly.
                        </div>
                    </div>
                </div>
            </header>

            <section className="mx-auto px-4 py-6">
                <TenantsProvider initialTenants={tenants}>
                    <TenantsPopupForm />
                    <TenantsList />
                </TenantsProvider>
            </section>
        </main>
    );
}
