"use client"

import Cookies from "js-cookie";
import React, {FormEvent, JSX, useState} from "react";
import {LogInIcon, ShieldIcon} from "lucide-react";

export default function LoginPage(): JSX.Element {
    const [error, setError] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);

    async function handleSubmit(e: FormEvent): Promise<void> {
        e.preventDefault();
        setError("");

        try {
            const url: string = "http://localhost:8080/api/auth/login";
            const response = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                const data: any = await response.json();
                const token: string = data.token;
                Cookies.set("authToken", token, {
                    expires: 1,
                    secure: true,
                    sameSite: "strict"
                });
                window.location.href = "/";
            } else {
                const errorRes: any = await response.json();
                setError(errorRes.message || "Login failed");
            }
        } catch (err: any) {
            setError("Something went wrong. Please try again.");
            console.error(err);
            console.log(err);
        }
    }

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4">
            <div className="w-full max-w-md bg-white rounded-lg shadow-md p-8">
                <h1 className="text-2xl font-semibold text-center text-gray-800 mb-6 flex justify-center items-center">
                    <LogInIcon className="mr-2" />
                    Přihlášení
                </h1>
                <form onSubmit={handleSubmit} className="space-y-6">
                    {error && (
                        <div className="p-3 bg-red-100 text-red-700 rounded">
                            {error}
                        </div>
                    )}

                    <div>
                        <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                            Uživatelské jméno
                        </label>
                        <input id="username" type="text" value={username} placeholder="Zadejte své uživatelské jméno"
                               onChange={(e): void => setUsername(e.target.value)}
                               required
                               className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150"/>
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                            Heslo
                        </label>
                        <div className="relative">
                            <input id="password" type={showPassword ? 'text' : 'password'} value={password}
                                   placeholder="Zadejte své heslo"
                                   onChange={(e) => setPassword(e.target.value)} required
                                   className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition duration-150"/>
                            <button type="button" onClick={(): void => setShowPassword(!showPassword)}
                                    className="absolute inset-y-0 right-0 px-3 flex items-center text-gray-600 hover:text-gray-800"
                                    aria-label={showPassword ? 'Skrýt heslo' : 'Zobrazit heslo'}>
                                {!showPassword ? (
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20"
                                         fill="currentColor">
                                        <path
                                            d="M10 3C5 3 1.73 7.11 1 10c.73 2.89 4 7 9 7s8.27-4.11 9-7c-.73-2.89-4-7-9-7zm0 12a5 5 0 110-10 5 5 0 010 10z"/>
                                        <path d="M10 7a3 3 0 100 6 3 3 0 000-6z"/>
                                    </svg>
                                ) : (
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20"
                                         fill="currentColor">
                                        <path
                                            d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"/>
                                    </svg>
                                )}
                            </button>
                        </div>
                    </div>

                    <button type="submit"
                            className="w-full bg-blue-600 text-white font-medium py-2 px-4 rounded-lg shadow-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-150">
                        Přihlásit se
                    </button>
                </form>

                <p className="flex justify-center align-middle text-sm text-gray-600 mt-6">
                    <ShieldIcon/> Vaše bezpečnost je pro nás prioritou. Vaše údaje jsou chráněny šifrováním a nejsou sdíleny s třetími stranami.
                </p>
            </div>
        </div>
    );
}
