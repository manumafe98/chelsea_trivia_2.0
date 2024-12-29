import axios from "axios";
import { Player } from "../types/player";

export const useAxios = async (url: string): Promise<Player[]> => {
    const response = await axios.get<Player[]>(url) ;
    return response.data;
};
