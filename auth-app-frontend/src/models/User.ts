export default interface User {
    id: string;
    email: string;
    username?: string;
    enabled: boolean;
    image?: string;
    createdAt?: string;
    updatedAt?: string;
    provider: string;
}