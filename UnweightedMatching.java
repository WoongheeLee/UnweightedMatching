import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

public class UnweightedMatching {
	public static ArrayList<String> CopyLines(String fileName) throws IOException {
		ArrayList<String> data = new ArrayList<String>();
		
		BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(fileName));

            String l;
            while ((l = inputStream.readLine()) != null) {
            	data.add(l);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        
        return data;
	}
	
	public static void isSharingEndPoints(UndirectedGraph M) {
		// ����� �Ǿ��ٸ�, 1�Ѵ� ��׸��� ���� ���ؽ��� ������ �ȵ�. ��������Ʈ�� ���� �ȵǹǷ�
		Iterator<String> itr = M.vertexSet().iterator();
		while(itr.hasNext()) {
			String v = itr.next();
			if (M.degreeOf(v) > 1) {
				System.out.println("Error: vertex "+v+" has "+M.degreeOf(v)+" degrees.");
			}
		}
	}
	
	public static UndirectedGraph<String, DefaultEdge> UnweightedMatching(UndirectedGraph<String, DefaultEdge> G) {
		UndirectedGraph<String, DefaultEdge> M = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		
		// Unweighted Matching�� ��Ʈ�������� �ϱ� ����
		// ��������Ʈ ���� ���ϴ� ������ ������ ��
		// �׷���, unweighted matching �׷����� �� ���ϰ� ����,
		// ���ؽ� ������ ���� �׷����� ����, ��� ���ؽ��� ��׸��� 1�� ���� �� ����
		Iterator<DefaultEdge> edges = G.edgeSet().iterator();
		
		// ��Ʈ�������� �����ȵǾ�����
		// �׷��� ��Ʈ������ vertex set�� �޸𸮿� �ö���ְ�,
		// ��Ʈ�������� edge�� �Է� �޴´ٴ� �� ����
		while(edges.hasNext()) {
			DefaultEdge edge = edges.next();
			String v1 = G.getEdgeSource(edge);
			String v2 = G.getEdgeTarget(edge);
			
			if (M.containsVertex(v1) || M.containsVertex(v2)) {
				// M�� � v�� ���� �ִٴ� ���� �̹� �� ������ ��������Ʈ �ϳ��� M�� ����ִٴ� ��
				// ���̻� ��������Ʈ�� ������ �����ϱ� ���� �� �Ѿ
				// ���� �¿� ���� iteration ���̱� ������
			} else {
				M.addVertex(v1);
				M.addVertex(v2);
				M.addEdge(v1, v2);
			}
		}
		
		return M;
	}
	
	public static void main(String[] args) throws IOException {
		String fileName = "./data/facebook_combined.txt";
		ArrayList<String> data = CopyLines(fileName);
		
		// G = (V,E)
		UndirectedGraph<String, DefaultEdge> G = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
				
		// ��ü �׷��� �����͸� ���� �׷��� �ڷ����� �־���
		for (int i = 0; i < data.size(); i++) {
			String[] edge = data.get(i).split(" ");
			if (edge.length < 2) {
				System.out.println("Error: Edge is not right!");
				System.exit(1);
			}
			String v1 = edge[0];
			String v2 = edge[1];
			
			G.addVertex(v1);
			G.addVertex(v2);
			G.addEdge(v1, v2);
		}
		
		// ���� �׷����� ���� ���
		System.out.println("���� �׷��� ���ؽ� ��: "+G.vertexSet().size());
		System.out.println("���� �׷��� ���� ��: "+G.edgeSet().size()+"\n");
			
		// Unweighted Matching�� ���� ���� �׷���
		UndirectedGraph<String, DefaultEdge> M = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

		// ���� �����̵� ���� �����ս� Ȯ��(��Ʈ����)
		double time = System.currentTimeMillis();
		M = UnweightedMatching(G);
		isSharingEndPoints(M);
		int nEdgesM = M.edgeSet().size();
		System.out.println("���� �����̵��� �˰���: "+nEdgesM);
		System.out.println("����ð�: "+((double)(System.currentTimeMillis()-time))+"\n");
	}
}
