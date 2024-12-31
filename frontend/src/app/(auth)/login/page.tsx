"use client"

import Cookies from "js-cookie";
import React, {FormEvent, JSX, useState} from "react";

export default function LoginPage(): JSX.Element {
    const [error, setError] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

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
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md bg-white rounded-lg shadow-md p-8">
                <h1 className="text-2xl font-semibold text-center text-gray-800 mb-6">Login</h1>
                <form onSubmit={handleSubmit} className="space-y-6">
                    {error && (
                        <p className="text-sm text-red-600">
                            {error}
                        </p>
                    )}

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Username</label>
                        <input type="text" value={username} onChange={(e): void => setUsername(e.target.value)} required
                               className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Password</label>
                        <input type="password" value={password} onChange={(e): void => setPassword(e.target.value)} required
                               className="mt-1 block w-full px-3 py-2 bg-gray-50 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500" />
                    </div>

                    <button type="submit" className="w-full bg-blue-600 text-white font-medium py-2 px-4 rounded-lg shadow-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                        Log In
                    </button>
                </form>
            </div>
        </div>
    );
}
