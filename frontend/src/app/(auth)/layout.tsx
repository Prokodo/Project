import "../globals.css";
import React from 'react';

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
