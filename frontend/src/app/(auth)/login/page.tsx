import React from "react";
import {Metadata} from "next";
import {LogInIcon, ShieldIcon} from "lucide-react";
import {LoginForm} from "@/components/auth/LoginForm";

export async function generateMetadata(): Promise<Metadata> {
    return {
        title: 'Login Page',
        description: 'Please log in to access your account.',
    };
}

async function LoginPage() {
    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
            <div className="w-full max-w-md bg-white rounded-lg shadow-md p-8">
                <h1 className="text-2xl font-semibold text-center text-gray-800 mb-6 flex justify-center items-center">
                    <LogInIcon className="mr-2" />
                    Přihlášení
                </h1>
                <LoginForm />

                <p className="flex justify-center align-middle text-sm text-gray-600 mt-6">
                    <ShieldIcon/> Vaše bezpečnost je pro nás prioritou. Vaše údaje jsou chráněny šifrováním a nejsou sdíleny s třetími stranami.
                </p>
            </div>
        </div>
    );
}

export default LoginPage;