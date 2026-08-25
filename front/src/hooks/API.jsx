import axios from "axios"
import { getToken } from "./Token";

const API = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        "Content-type": "application/json"
    }
});

API.interceptors.request.use(config => {
    const accessToken = getToken();
    
    if(accessToken) {
        config.headers.Authorization = accessToken;
    }
    return config;
});

export default API;