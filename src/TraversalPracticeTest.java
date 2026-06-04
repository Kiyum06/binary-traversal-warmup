import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TraversalPracticeTest {

     /**
     * Splits the captured output into lines, handling different newline conventions.
     */
    private List<String> getLines(String output) {
        String trimmed = output.trim();
        if (trimmed.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(trimmed.split("\\R"));
    }

    // Tests for printNegativeNodesInOrder

    @Test
    public void testPrintNegativeNodesInOrder_ComplexTree() {
        TreeNode node = new TreeNode(-3,
            new TreeNode(4,
                new TreeNode(1001),
                new TreeNode(9999,
                    new TreeNode(8888, null, new TreeNode(-2)),
                    new TreeNode(7777)
                )
            ),
            new TreeNode(5000,
                new TreeNode(-9, new TreeNode(-5), null),
                new TreeNode(-3000)
            )
        );
        String output = captureOutput(() -> TraversalPractice.printNegativeNodesInOrder(node));
        List<String> lines = getLines(output);
        List<String> expected = Arrays.asList("-2", "-3", "-5", "-9", "-3000");
        assertEquals(expected, lines);
    }

    @Test
    public void testPrintNegativeNodesInOrder_SingleNodeMatch() {
        TreeNode node = new TreeNode(-5);
        String output = captureOutput(() -> TraversalPractice.printNegativeNodesInOrder(node));
        List<String> lines = getLines(output);
        assertEquals(Collections.singletonList("-5"), lines);
    }

    @Test
    public void testPrintNegativeNodesInOrder_SingleNodeNoMatch() {
        TreeNode node = new TreeNode(0);
        String output = captureOutput(() -> TraversalPractice.printNegativeNodesInOrder(node));
        List<String> lines = getLines(output);
        assertTrue(lines.isEmpty());
    }

    @Test
    public void testPrintNegativeNodesInOrder_NullNode() {
        String output = captureOutput(() -> TraversalPractice.printNegativeNodesInOrder(null));
        List<String> lines = getLines(output);
        assertTrue(lines.isEmpty());
    }

    @Test
    public void testPrintNegativeNodesInOrder_BalancedTree() {
        TreeNode node = new TreeNode(-1,
            new TreeNode(-3),
            new TreeNode(-2)
        );
        String output = captureOutput(() -> TraversalPractice.printNegativeNodesInOrder(node));
        List<String> lines = getLines(output);
        List<String> expected = Arrays.asList("-3", "-1", "-2");
        assertEquals(expected, lines);
    }

    /**
     * Tree:
     *          9
     *         / \
     *        2   5
     *       / \   \
     *      7   1   3
     *     /       / \
     *    4       8  33
     *     \      /  \
     *      6    0    77
     */
    @Test
    public void testSampleTree_sumLeafNodes() {
        TreeNode root = new TreeNode(9,
            new TreeNode(2,
                new TreeNode(7,
                    new TreeNode(4, null, new TreeNode(6)),
                    null
                ),
                new TreeNode(1)
            ),
            new TreeNode(5,
                null,
                new TreeNode(3,
                    new TreeNode(8),
                    new TreeNode(33, new TreeNode(0), new TreeNode(77))
                )
            )
        );
        assertEquals(92, TraversalPractice.sumLeafNodes(root));
    }

    /**
     * Tree:
     *     42
     */
    @Test
    public void testSingleNode_sumLeafNodes() {
        TreeNode root = new TreeNode(42);
        assertEquals(42, TraversalPractice.sumLeafNodes(root));
    }

    /**
     * Tree:
     *     1
     *    /
     *   2
     */
    @Test
    public void testLeftOnlyChild_sumLeafNodes() {
        TreeNode root = new TreeNode(1, new TreeNode(2), null);
        assertEquals(2, TraversalPractice.sumLeafNodes(root));
    }

    /**
     * Tree:
     *   1
     *    \
     *     3
     */
    @Test
    public void testRightOnlyChild_sumLeafNodes() {
        TreeNode root = new TreeNode(1, null, new TreeNode(3));
        assertEquals(3, TraversalPractice.sumLeafNodes(root));
    }

    /**
     * Tree: null (empty tree)
     */
    @Test
    public void testNullInput_sumLeafNodes() {
        assertEquals(0, TraversalPractice.sumLeafNodes(null));
    }

    /**
     * Tree:
     *   1
     *    \
     *     2
     *      \
     *       3
     */
    @Test
    public void testRightChain_sumLeafNodes() {
        TreeNode root = new TreeNode(1, null,
            new TreeNode(2, null,
                new TreeNode(3)
            )
        );
        assertEquals(3, TraversalPractice.sumLeafNodes(root));
    }

    /**
     * Tree:
     *       10
     *      /  \
     *     5    15
     *         /  \
     *        12   20
     */
    @Test
    public void testMultipleLeavesMixed_sumLeafNodes() {
        TreeNode root = new TreeNode(10,
            new TreeNode(5),
            new TreeNode(15,
                new TreeNode(12),
                new TreeNode(20)
            )
        );
        assertEquals(5 + 12 + 20, TraversalPractice.sumLeafNodes(root));
    }

        /** The below are utility classes that help with testing. You do not need to modify them, but you can look with interest! */
    /**
     * Simple PrintStream that writes output to both the console and an internal buffer.
     */
    private static class TeePrintStream extends PrintStream {
        private final PrintStream second;

        public TeePrintStream(PrintStream main, PrintStream second) {
            super(main);
            this.second = second;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            super.write(buf, off, len);
            second.write(buf, off, len);
        }

        @Override
        public void write(int b) {
            super.write(b);
            second.write(b);
        }
    }

    /**
     * Captures the output of a Runnable while still printing to the console.
     * @param runnable the code that prints to System.out
     * @return the captured output as a String
     */
    private String captureOutput(Runnable runnable) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream second = new PrintStream(baos, true);
        TeePrintStream tee = new TeePrintStream(originalOut, second);
        System.setOut(tee);
        try {
            runnable.run();
        } finally {
            System.out.flush();
            System.setOut(originalOut);
        }
        return baos.toString();
    }
}
