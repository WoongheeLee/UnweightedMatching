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
		// 제대로 되었다면, 1넘는 디그리를 갖는 버텍스가 있으면 안됨. 엔드포인트는 공유 안되므로
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
		
		// Unweighted Matching을 스트리밍으로 하기 위해
		// 엔드포인트 공유 안하는 엣지만 넣으면 됨
		// 그러면, unweighted matching 그래프를 다 구하고 나면,
		// 버텍스 개수는 원본 그래프와 같고, 모든 버텍스의 디그리는 1로 동일 할 것임
		Iterator<DefaultEdge> edges = G.edgeSet().iterator();
		
		// 스트리밍으로 구현안되어있음
		// 그래프 스트리밍은 vertex set이 메모리에 올라와있고,
		// 스트리밍으로 edge를 입력 받는다는 점 주의
		while(edges.hasNext()) {
			DefaultEdge edge = edges.next();
			String v1 = G.getEdgeSource(edge);
			String v2 = G.getEdgeTarget(edge);
			
			if (M.containsVertex(v1) || M.containsVertex(v2)) {
				// M이 어떤 v를 갖고 있다는 말은 이미 이 엣지의 엔드포인트 하나가 M에 들어있다는 말
				// 더이상 엔드포인트의 공유를 금지하기 위해 걍 넘어감
				// 엣지 셋에 관해 iteration 중이기 때문에
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
				
		// 전체 그래프 데이터를 원본 그래프 자료형에 넣어줌
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
		
		// 원본 그래프의 구조 출력
		System.out.println("원본 그래프 버텍스 수: "+G.vertexSet().size());
		System.out.println("원본 그래프 엣지 수: "+G.edgeSet().size()+"\n");
			
		// Unweighted Matching을 위한 서브 그래프
		UndirectedGraph<String, DefaultEdge> M = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

		// 수업 슬라이드 내용 퍼포먼스 확인(스트리밍)
		double time = System.currentTimeMillis();
		M = UnweightedMatching(G);
		isSharingEndPoints(M);
		int nEdgesM = M.edgeSet().size();
		System.out.println("수업 슬라이드의 알고리즘: "+nEdgesM);
		System.out.println("수행시간: "+((double)(System.currentTimeMillis()-time))+"\n");
	}
}
