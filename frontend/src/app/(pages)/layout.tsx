import "../globals.css";
import React from "react";
import {Toaster} from "sonner";
import type {Metadata} from "next";
import {AppSidebar} from "@/components/sidebar/AppSidebar";
import {SidebarProvider, SidebarTrigger} from "@/components/ui/sidebar";

export const metadata: Metadata = {
    title: "Create Next App",
    description: "Generated by create next app",
};

export default async function RootLayout({children}: Readonly<{ children: React.ReactNode }>) {
    return (
        <html lang="en">
            <body>
                <SidebarProvider>
                    <AppSidebar />
                    <main className="w-full">
                        <SidebarTrigger />
                        {children}
                    </main>
                </SidebarProvider>
                <Toaster />
            </body>
        </html>
    );
}
