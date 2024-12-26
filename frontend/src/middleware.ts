import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'
import {RequestCookie} from "next/dist/compiled/@edge-runtime/cookies";
import {redirect} from "next/navigation";

export function middleware(request: NextRequest) {
    let cookie: RequestCookie | undefined = request.cookies.get('authToken')
    if (!cookie && !request.url.endsWith("/login")) {
        return NextResponse.redirect(new URL('/login', request.url));
    }
    return NextResponse.next();
}


export const config = {
    matcher: [
        // Apply middleware only to these paths
        '/properties',
    ],
};