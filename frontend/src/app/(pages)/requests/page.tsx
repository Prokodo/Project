import {ReactElement} from "react";
import {redirect} from "next/navigation";
import {getAuthToken} from "@/utils/auth";
import {getUserRoles, ValidRoles} from "@/services/global";
import {fetchProperties} from "@/services/properties";
import {RolesResponse} from "@/types/types";
import RequestsList from "@/components/requests/RequestsList";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import {HelpCircleIcon} from "lucide-react";
import UserRequestList from "@/components/requests/UserRequestList";
import {RequestProvider} from "@/components/requests/RequestsContext";
import {fetchRequests} from "@/services/requests";
import RequestPopupForm from "@/components/requests/RequestPopupForm";
import {Metadata} from "next";

export async function generateMetadata(): Promise<Metadata> {
    return {
        title: 'TenantFlow | Requests',
    };
}

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const authResponse: RolesResponse | undefined = await getUserRoles(authToken);
    if (!authResponse?.loggedIn) {
        redirect('/login');
    }

    const isPrivileged: boolean = authResponse?.roles?.some((role: ValidRoles): boolean => ["ROLE_ADMIN", "ROLE_MANAGER"].includes(role)) || false;
    return (
        <main className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white py-4 shadow-md">
                <div className="mx-auto px-10 flex items-center justify-between">
                    <h1 className="text-2xl font-bold">Requests management</h1>

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
                            Tip: Managing Properties <br/>
                            If you need help managing your properties—such as adding new listings, updating details, or
                            addressing maintenance requests—please
                            contact the administrator assigned to your account. They can assist you and ensure
                            everything is handled effectively.
                        </div>
                    </div>
                </div>
            </header>

            <section className="mx-auto px-4 py-6">
                {isPrivileged?
                    <PropertiesProvider initialProperties={await fetchProperties(authToken) || []}>
                        <RequestsList />
                    </PropertiesProvider>
                    :
                    <RequestProvider initialRequests={await fetchRequests(authToken) || []}>
                        <RequestPopupForm properties={await fetchProperties(authToken) || []} />
                        <UserRequestList />
                    </RequestProvider>
                }
            </section>
        </main>
    );
}