import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { motion } from "framer-motion";
import { Github } from "lucide-react";

export default function Login() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-background px-6">
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="w-full max-w-md"
      >
        <Card className="bg-card/70 backdrop-blur-xl border-border rounded-2xl shadow-xl">
          <CardContent className="p-8">
            <h1 className="text-3xl font-bold text-center mb-2">
              Welcome Back
            </h1>
            <p className="text-center text-muted-foreground mb-8">
              Log in to continue securing your app
            </p>

            {/* OAuth Buttons */}
            <div className="space-y-3 mb-6">
              <Button
                variant="outline"
                size="lg"
                className="w-full rounded-2xl flex items-center gap-3"
              >
                <img
                  src="https://www.svgrepo.com/show/475656/google-color.svg"
                  alt="Google"
                  className="w-5 h-5"
                />
                Continue with Google
              </Button>

              <Button
                variant="outline"
                size="lg"
                className="w-full rounded-2xl flex items-center gap-3"
              >
                <Github className="w-5 h-5" />
                Continue with GitHub
              </Button>
            </div>

            {/* Divider */}
            <div className="flex items-center gap-4 my-6">
              <div className="flex-1 h-px bg-border" />
              <span className="text-sm text-muted-foreground">
                or continue with
              </span>
              <div className="flex-1 h-px bg-border" />
            </div>

            {/* Email / Password Form */}
            <form className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="you@example.com"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  placeholder="••••••••"
                />
              </div>

              <Button
                type="submit"
                size="lg"
                className="w-full rounded-2xl text-lg"
              >
                Login
              </Button>
            </form>

            <div className="mt-6 text-center text-sm text-muted-foreground">
              Don’t have an account?{" "}
              <a href="/signup" className="text-primary hover:underline">
                Create one
              </a>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
