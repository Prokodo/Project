import {Metadata} from "next";
import {redirect} from "next/navigation";
import {getAuthToken} from "@/utils/auth";
import {RolesResponse} from "@/types/types";
import {getUserRoles, ValidRoles} from "@/services/global";
import UserIndexPage from "@/components/index/UserIndexPage";
import AdminIndexPage from "@/components/index/AdminIndexPage";

export async function generateMetadata(): Promise<Metadata> {
    return {
        title: 'TenantFlow | Index',
    };
}

async function Home() {
    const authToken: string | undefined = await getAuthToken();
    const authResponse: RolesResponse | undefined = await getUserRoles(authToken);
    if (!authResponse?.loggedIn) {
    redirect('/login');
    }

    const isPrivileged: boolean = authResponse?.roles?.some((role: ValidRoles): boolean => ["ROLE_ADMIN", "ROLE_MANAGER"].includes(role)) || false;
    return (
      <>
          {isPrivileged?
              <AdminIndexPage/> :
              <UserIndexPage/>
          }
      </>
    );
}

export default Home;