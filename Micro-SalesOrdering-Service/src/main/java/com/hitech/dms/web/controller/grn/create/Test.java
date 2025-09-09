package com.hitech.dms.web.controller.grn.create;

import java.util.Stack;

public class Test {
	
	public static boolean find132pattern(int[] nums) {
        int n = nums.length;
        if (n < 3) return false;

        Stack<Integer> stack = new Stack<>();
        int second = Integer.MIN_VALUE;  // Best candidate for nums[k] (the "2" in the pattern)

        // Traverse the array from the end (right to left)
        for (int i = n - 1; i >= 0; i--) {

            // If we find a current number that is less than 'second', we've found the 132 pattern
            if (nums[i] < second) {
                return true;  // nums[i] = "1", second = "2", stack contained "3"
            }

            // While current number is greater than what's on top of the stack,
            // that means we can pop it and make it the new 'second' (a valid "2")
            while (!stack.isEmpty() && nums[i] > stack.peek()) {
                second = stack.pop();  // This popped value is a new best candidate for "2"
            }

            // Push current number as potential "3" in the future
            stack.push(nums[i]);
        }

        // No pattern found
        return false;
    }
	
	public static void main(String[] args) {
		
		
		int[] nums1 = {1, 2, 3, 4};
        int[] nums2 = {3, 1, 4, 2};
        int[] nums3 = {-1, 3, 2, 0};
        
        //System.out.println(find132pattern(nums1));  // false
        System.out.println(find132pattern(nums2));  // true (1 < 2 < 4)
        //System.out.println(find132pattern(nums3));  // true (-1 < 0 < 3)
		
	}

}
