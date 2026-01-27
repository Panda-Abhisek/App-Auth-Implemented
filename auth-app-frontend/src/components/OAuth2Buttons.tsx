import { NavLink } from 'react-router'
import { Button } from './ui/button'
import { Github } from 'lucide-react'

const OAuth2Buttons = () => {
    return (
        <div className="space-y-3 mb-6">
            <NavLink to={`${import.meta.env.VITE_BASE_URL || "http://localhost:8083"}/oauth2/authorization/google`} className={"block"}>
                <Button
                    type='button'
                    variant="outline"
                    size="lg"
                    className="w-full cursor-pointer rounded-2xl flex items-center gap-3"
                >
                    <img
                        src="https://www.svgrepo.com/show/475656/google-color.svg"
                        alt="Google"
                        className="w-5 h-5"
                    />
                    Continue with Google
                </Button>
            </NavLink>

            <NavLink to={`${import.meta.env.VITE_BASE_URL || "http://localhost:8083"}/oauth2/authorization/github`} className={"block"}>
                <Button
                    type='button'
                    variant="outline"
                    size="lg"
                    className="w-full cursor-pointer rounded-2xl flex items-center gap-3"
                >
                    <Github className="w-5 h-5" />
                    Continue with GitHub
                </Button>
            </NavLink>
        </div>
    )
}

export default OAuth2Buttons