/**
 *   The Percolation class models an abstract process known as percolation. 
 *   The percolation process can be modeled as a dynmaic connectivity problem.
 *   For more details refer to the coursera Algorithms course.
 */


import java.util.Arrays;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;



public class Percolation {
	
	private int dimSize;								// size of grid
	private int[][] openSites;							// keeps track of open sites
	private WeightedQuickUnionUF quickUnionStructure;	// data structure to keep track of site connections
	
	private static final int OPEN = 0;					
	private static final int BLOCKED = 1;
	
	private int topVirtualSite;							// used by percolates() to check whether system percolates.
	private int bottomVirtualSite;
	
	public Percolation(int n) throws java.lang.IllegalArgumentException{
		if(n < 0) {
			throw new java.lang.IllegalArgumentException();
		}
		dimSize = n;
		openSites = new int[dimSize][dimSize];
		quickUnionStructure = new WeightedQuickUnionUF(dimSize * dimSize + 2);
		
		topVirtualSite = 0;
		bottomVirtualSite = dimSize * dimSize + 1;
		
		// intialize all sites to be closed. 1 = BLOCKED, 0 = OPEN
		for(int i=0; i<openSites.length;i++){
			for(int j=0; j<openSites.length;j++){
				openSites[i][j] = 1;
			}
		}
	}
	
	/**
	 * Used to open a site. First marks site as open then proceeds to union with any open neighbors.
	 * @param row index
	 * @param col index
	 */
	public void open(int row, int col){
		
		if (isValidDimensionValues(row, col)){	
			
			int indexOfCurrentSite = xyTo1D(row, col);
			
			if(openSites[row-1][col-1] == BLOCKED){	// if blocked site
				
				openSites[row-1][col-1] = OPEN;	// open the site
				
				if(row == 1){		//connect to TOP virtual site since its first row
					quickUnionStructure.union(topVirtualSite, indexOfCurrentSite);
				}
				
				if(row == dimSize){	// connect to bottom virtual since its last row
					quickUnionStructure.union(bottomVirtualSite, indexOfCurrentSite);
				}
				
				//to union with any open neighbor sites.
				if(col>1 && isOpen(row, col-1)){
					quickUnionStructure.union(indexOfCurrentSite, xyTo1D(row, col-1));
				}
				
				if(col<dimSize && isOpen(row, col+1)){
					quickUnionStructure.union(indexOfCurrentSite, xyTo1D(row, col+1));
				}
				
				if(row>1 && isOpen(row-1, col)){
					quickUnionStructure.union(indexOfCurrentSite, xyTo1D(row-1, col));
				}
				
				if(row<dimSize && isOpen(row+1, col)){
					quickUnionStructure.union(indexOfCurrentSite, xyTo1D(row+1, col));
				}				
			}	
		}
		else{	// not valid dimension values
			throw new java.lang.IndexOutOfBoundsException();
		}
	}
	
	/**
	 * check whether a site is open or not
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isOpen(int row, int col){
		if(isValidDimensionValues(row, col)){
			return openSites[row-1][col-1] == 0;
		}
		else{
			throw new java.lang.IndexOutOfBoundsException();
		}
	}
	
	/**
	 * checks whether a site connects to the top of the grid.
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isFull(int row, int col){
		if(isValidDimensionValues(row, col)){
			return quickUnionStructure.connected(topVirtualSite, xyTo1D(row, col));
		}
		else{
			throw new java.lang.IndexOutOfBoundsException();
		}
	}
	
	/**
	 * checks whether the system percolates.
	 * @return
	 */
	public boolean percolates(){
		return quickUnionStructure.connected(topVirtualSite, bottomVirtualSite);
	}
	
	/**
	 * converts supplied 2D representation of site to 1D representation 
	 * as needed by the underlying WeightQuickUnionUf data structure
	 * @param row
	 * @param col
	 * @return
	 */
	private int xyTo1D(int row, int col){
			return (row-1) * (dimSize) + col;
	}
	
	/**
	 * checks whether the dimensions are correct. 1 indexed.
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isValidDimensionValues(int row, int col){
		return (  (row > 0 && row <= dimSize) && (col > 0 && col <= dimSize) );
	}

	/*
	//for testing only. not specified in API
	public void printOpenSites(){
		for(int[] arr : openSites){
			System.out.println(Arrays.toString(arr));
		}
		
		System.out.println("");
	}
	*/
	/*
	//for testing only. not specified in API
	public boolean isConnected(int row, int col, int row1, int col2){
		int index1 = xyTo1D(row, col);
		int index2 = xyTo1D(row1, col2);
		
		return quickUnionStructure.connected(index1, index2);
	}
	*/
	
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int size = 4;
		Percolation p = new Percolation(size);
		
		
		
		p.open(1,3);
		p.open(3,3);
		p.open(1,1);
		p.open(2,1);
		p.open(1,1);
		p.open(2,3);
		p.open(4,4);
		p.open(3,4);
		//p.printOpenSites();
		
		
		System.out.println("isFull: " + p.percolates());
		
	}*/
}
