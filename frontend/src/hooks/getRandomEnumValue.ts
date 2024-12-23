export const getRandomEnumValue = <T extends Record<string, string>>(enumType: T): T[keyof T] => {
    const enumValues = Object.values(enumType) as T[keyof T][];
    const randomIndex = Math.floor(Math.random() * enumValues.length);
    return enumValues[randomIndex];
};
