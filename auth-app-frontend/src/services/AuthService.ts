import api from "@/config/ApiClient";
import type RegisterData from "@/models/RegisterData";

export const registerUser = async (signupData: RegisterData) => {
    const response = await api.post(`/auth/register`, signupData);
    return response.data;
}