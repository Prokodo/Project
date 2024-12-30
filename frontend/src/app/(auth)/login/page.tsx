"use client"

import Cookies from "js-cookie";
import {useRouter} from "next/navigation";
import React, {FormEvent, JSX, useState} from "react";
import {AppRouterInstance} from "next/dist/shared/lib/app-router-context.shared-runtime";

export default function LoginPage(): JSX.Element {
    const [error, setError] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const router: AppRouterInstance = useRouter();

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
                router.push("/");
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
        <div style={{ maxWidth: 400, margin: "auto" }}>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <label>Username</label>
                <input type="text" value={username} onChange={(e): void => setUsername(e.target.value)} required />
                <br />

                <label>Password</label>
                <input type="password" value={password} onChange={(e): void => setPassword(e.target.value)} required />
                <br />

                <button type="submit">Log In</button>
                {error && <p style={{ color: "red" }}>{error}</p>}
            </form>
        </div>
    );
}
