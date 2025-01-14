import {getAuthToken} from "@/utils/auth";
import {RolesResponse} from "@/types/types";
import {getUserRoles} from "@/services/global";
import {redirect} from "next/navigation";
import {Metadata} from "next";
import AdminIndexPage from "@/components/index/AdminIndexPage";
import UserIndexPage from "@/components/index/UserIndexPage";

export async function generateMetadata(): Promise<Metadata> {
    return {
        title: 'TenantFlow | Index',
    };
}

async function Home() {
  const authToken: string | undefined = await getAuthToken();
  const roles: RolesResponse | undefined = await getUserRoles(authToken);
  if (!roles?.loggedIn) {
    redirect('/login');
  }

  const isAdmin: boolean = roles.roles.includes("ROLE_ADMIN");

  return (
      <>
          {isAdmin?
              <AdminIndexPage/>
              :
              <UserIndexPage/>
          }
      </>
  );
}

export default Home;