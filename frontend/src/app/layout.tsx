import "./globals.css";
import type {Metadata} from "next";
import {AppSidebar} from "@/components/sidebar/AppSidebar";
import {SidebarProvider, SidebarTrigger} from "@/components/ui/sidebar";

export const metadata: Metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

export default function RootLayout({children}: Readonly<{ children: React.ReactNode }>) {
    return (
        <html lang="en">
            <body>
                <SidebarProvider>
                    <AppSidebar />
                    <main>
                        <SidebarTrigger />
                    {children}
                    </main>
                </SidebarProvider>
            </body>
        </html>
    );
}