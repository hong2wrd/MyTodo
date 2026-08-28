import axios from "axios"
import { getToken, removeToken, setToken } from "./useToken";

const config = {
    baseURL: 'http://localhost:8080',
    headers: {
        "Content-type": "application/json"
    },
    withCredentials: true
}

const API = axios.create(config);

/**
 * API 요청 전
 */
API.interceptors.request.use(config => {
    const accessToken = getToken();
    
    if(accessToken) {
        config.headers.Authorization = accessToken;
    }
    return config;
});

/**
 * API 요청 후
 */
API.interceptors.response.use(
    response => {
        return response;
    },
    async error => {
        const originalRequest = error.config;
        
        if (error.response?.status === 401 && !originalRequest._retry) {

            originalRequest._retry = true;

            try {
                const newAccessToken = await refreshAccessToken();
                
                setToken(newAccessToken);
                
                originalRequest.headers.Authorization = newAccessToken

                return API(originalRequest); // 기존 요청을 재요청

            } catch (refreshError) {
                // Refresh Token도 만료
                removeToken();

                window.location.replace("/login");

                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }

);

/**
 * refresh token으로 신규 access Token 발급
 * @returns Access Token
 */
const refreshAccessToken = async () => {
    const response = await axios.create(config)
        .post("/auth/refresh", {}, { withCredentials: true } );

    return response.data;
};

export default API;
