import {ReactElement} from "react";
import {Invoice, RolesResponse} from "@/types/types";
import {fetchInvoices} from "@/services/invoices";
import InvoiceList from "@/components/invoices/InvoiceList";
import {getAuthToken} from "@/utils/auth";
import {InvoiceProvider} from "@/components/invoices/InvoiceContext";
import {getUserRoles} from "@/services/global";
import {redirect} from "next/navigation";
import {HelpCircleIcon} from "lucide-react";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }

    const invoices: Invoice[] = await fetchInvoices(authToken) || [];
    return (
        <main className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white py-4 shadow-md">
                <div className="mx-auto px-10 flex items-center justify-between">
                    <h1 className="text-2xl font-bold">Invoice Management</h1>

                    <div className="relative group">
                        <HelpCircleIcon className="h-6 w-6 cursor-pointer"/>
                        <div  className="absolute top-full mt-2 w-max max-w-xs text-sm text-white bg-gray-800 rounded-md py-2 px-4 opacity-0 group-hover:opacity-100 transition-opacity duration-200 shadow-lg"
                              style={{
                                 right: "0",
                                 whiteSpace: "normal",
                                 overflowWrap: "break-word",
                                 transform: "translateX(0)",
                            }}>
                            If you have any issues with invoices or the app, please contact the admin responsible for your account.
                        </div>
                    </div>
                </div>
            </header>

            <section className="mx-auto px-4 py-6">
                <InvoiceProvider initialInvoices={invoices}>
                    <InvoiceList roles={roles?.roles || []}/>
                </InvoiceProvider>
            </section>
        </main>
    );
}
