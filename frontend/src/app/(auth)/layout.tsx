import "../globals.css";
import React from 'react';

export const metadata = {
    title: "Login - My Next App",
    description: "User login page",
};

export default function LoginLayout({ children }: { children: React.ReactNode }) {
    return (
        <html lang="en">
            <body>
                <div className="flex items-center justify-center min-h-screen bg-gray-100">
                    {children}
                </div>
            </body>
        </html>
    );
}
