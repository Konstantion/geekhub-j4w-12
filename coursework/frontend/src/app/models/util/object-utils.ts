export class ObjectUtils {
    private constructor() {

    }

    static replaceEmptyWithNull<T>(obj: T): T {
        if (typeof obj !== 'object' || obj === null) {
          return obj;
        }
    
        if (Array.isArray(obj)) {
          return obj.map((item) => this.replaceEmptyWithNull(item)) as any;
        }
    
        const result: Partial<T> = {};
    
        for (const key in obj) {
          if (obj.hasOwnProperty(key)) {
            const value = obj[key];
            if (value === '' || value === undefined) {
              result[key] = null;
            } else {
              result[key] = this.replaceEmptyWithNull(value);
            }
          }
        }
    
        return result as T;
      }
}