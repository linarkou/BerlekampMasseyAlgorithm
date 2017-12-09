import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] s = new int[]{0,1,1,0,0,0,1,1,1,1,0,0}; //input sequence
        int osn = 2; //order of finite field
        Map.Entry<int[],Integer> res = bmalgo(s, osn);
        int L = res.getValue();
        int[] LFSR = res.getKey();
        int k = 10; //count of symbols to generate
        printNextSeq(s,osn,L,LFSR,k);
    }

    private static void printNextSeq(int[] sourceSeq, int osn, int L, int[] lfsr, int count) {
        int[] registr = new int[L];
        for (int i = 0 ; i < L; ++i) {
            registr[i] = sourceSeq[lfsr.length-i-1];
        }
        System.out.println("Next "+count+" symbols of sequence:");
        for (int i = 0; i < count; ++i) {
            int next_s = 0;
            for (int j = 0; j < L; ++j) {
                next_s = (next_s + ((lfsr[j+1]*registr[j]) % osn)) % osn;
            }
            for (int j = L-1; j > 0; --j)
                registr[j] = registr[j-1];
            registr[0] = next_s;
            System.out.print(next_s+" ");
        }
    }

    public static Map.Entry<int[],Integer> bmalgo(int[] s, int osn) {
        int r=0,delta=0,L=0;
        int n = s.length;
        int[] B = new int[n];
        B[0] = 1;
        int[] Lambda = new int[n];
        Lambda[0] = 1;
        int[] T = new int[n];
        System.out.println(String.format("| r| s|delta|%50s|%50s|L|","B(x)","Lambda(x)"));
        System.out.println(String.format("%0" + 117 + "d", 0).replace("0","-"));
        print(r,-1,delta,B,Lambda,L);
        for (r = 1; r <= n; ++r) {
            delta = 0;
            for (int j = 0; j <= L; ++j) //вычисление delta
                delta = (delta + ((Lambda[j]*s[r-j-1]) % osn)) % osn;

            for (int i = n-1; i > 0; --i) //сдвиг
                B[i] = B[i-1];
            B[0] = 0;

            if (delta != 0) {
                for (int i = 0; i < n; ++i) { // T = Lambda + delta*B
                    T[i] = (Lambda[i] + (((osn-delta)*B[i]) % osn)) % osn;
                }
                if (2*L <= r - 1) {
                    for (int i = 0; i < n; ++i)  // B = delta*Lambda
                        B[i] = Lambda[i]*delta % osn;
                    L = r - L;
                }
                Lambda = Arrays.copyOf(T, n); //Lambda = T
            }
            print(r,s[r-1],delta,B,Lambda,L);
        }
        return new AbstractMap.SimpleEntry<int[], Integer>(Lambda, L);
    }

    public static void print(int r, int s, int delta, int[] B, int[] Lambda, int L) {
        System.out.println(String.format("|%2d|%2d|%5d|%50s|%50s|%d|",r,s,delta,polyToStr(B),polyToStr(Lambda),L));
    }

    public static String polyToStr(int[] P) {
        String res = "";
        for (int i = 0; i < P.length; ++i) {
            if (P[i] == 0)
                continue;
            if (res.length() != 0)
                res += " + ";
            if (i == 0)
                res += P[i];
            else
                res += P[i] + "x^" + i;
        }
        return res;
    }
}