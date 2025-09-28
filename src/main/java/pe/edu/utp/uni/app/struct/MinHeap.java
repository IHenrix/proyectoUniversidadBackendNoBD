package pe.edu.utp.uni.app.struct;

import java.util.ArrayList;
import java.util.Comparator;

public class MinHeap<T> {
    private final ArrayList<T> a = new ArrayList<>();
    private final Comparator<T> cmp;

    public MinHeap(Comparator<T> cmp){ this.cmp = cmp; }
    public int size(){ return a.size(); }
    public boolean isEmpty(){ return a.isEmpty(); }

    public void push(T v){
        a.add(v);
        siftUp(a.size()-1);
    }
    public T peek(){ return a.isEmpty()? null : a.get(0); }
    public T pop(){
        if (a.isEmpty()) return null;
        T top = a.get(0);
        T last = a.remove(a.size()-1);
        if (!a.isEmpty()){ a.set(0, last); siftDown(0); }
        return top;
    }
    private void siftUp(int i){
        while(i>0){
            int p=(i-1)/2;
            if (cmp.compare(a.get(i), a.get(p))>=0) break;
            swap(i,p); i=p;
        }
    }
    private void siftDown(int i){
        int n=a.size();
        while(true){
            int l=2*i+1, r=l+1, m=i;
            if (l<n && cmp.compare(a.get(l), a.get(m))<0) m=l;
            if (r<n && cmp.compare(a.get(r), a.get(m))<0) m=r;
            if (m==i) break;
            swap(i,m); i=m;
        }
    }
    private void swap(int i,int j){ T t=a.get(i); a.set(i,a.get(j)); a.set(j,t); }
}
